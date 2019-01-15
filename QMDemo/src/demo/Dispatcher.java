package demo;

import demo.callback.ResultCallBack;
import demo.dataprocessor.Processor;
import demo.task.decode.DecodeTask;
import demo.task.file.FilesTask;
import demo.task.file.FilesTaskEvent;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author: KevinWu
 * date: 2019/1/14
 * description: 文件读取与解析任务分发器
 */

public final class Dispatcher {
    private final int poolSize;//默认线程数
    private final int maxTask;//最大执行任务数，当执行完毕后关闭线程池
    private int nowExecutedTask = 0;
    private int addTask = 0;
    private ExecutorService executorService;
    private final DecodeTask decodeTask;
    private final Deque<FilesTask> waitingFilesTask = new ArrayDeque<>();//文件队列
    private final Deque<FilesTask> processingFilesTask = new ArrayDeque<>();//正在处理的文件队列
    private final Object lock = new Object();

    private final FilesTaskEvent filesTaskEvent = new FilesTaskEvent() {
        @Override
        public void onFileReadResult(String result) {
            decodeTask.add(result);
        }

        @Override
        public void onFileReadException(FilesTask task, String msg) {
            // 由于正常情况下文件读取不会出异常，也不是此题考查重点，为了代码结构更清晰，此demo不做此情况处理
            // 如果要处理，思路如下：
            // 1. 新建一个Deque<File> exceptionFilesTask
            // 2. 出现异常可以将文件重新放入异常等待队列exceptionFilesTask
            // 3. 在所有文件处理完后调用enqueueTsk(FilesTask task)重试异常文件读取
            //   （需要注意的是要在所有task执行完后增加maxTask值为exceptionFilesTask的size
            //     或者增加一个额外变量如(int)exceptionTask用于记录异常任务个数）
            // 4. 关闭线程池和DecodeTask需增加exceptionFilesTask队列size判断
        }

        @Override
        public void onFileReadFinish(FilesTask task) {
            synchronized (lock) {
                processingFilesTask.remove(task);
                if (nowExecutedTask >= maxTask && processingFilesTask.size() == 0) {
                    //当所有任务执行完，关闭线程池和decodeTask
                    shutdownExecutor();
                    decodeTask.finish();
                    lock.notifyAll();
                }
            }
            handleFilesTask();
        }
    };

    /**
     * 如果不通过Builder构建Dispatcher，将使用默认值
     */
    public Dispatcher() {
        this(new Builder());
    }

    private Dispatcher(Builder builder) {
        this.poolSize = builder.poolSize;
        this.maxTask = builder.maxTask;
        decodeTask = new DecodeTask(lock, builder.dataProcessor, builder.resultCallBack);
    }

    public void enqueueTask(FilesTask task) throws IllegalAccessException {
        synchronized (lock) {
            addTask++;
            if (addTask <= maxTask) {
                task.setFilesTaskEvent(filesTaskEvent);
                waitingFilesTask.add(task);
            } else {
                throw new IllegalAccessException("任务执行数超出设置的最大值");
            }
        }
        handleFilesTask();
    }

    private void handleFilesTask() {
        List<FilesTask> executableTaskList = new ArrayList<>();
        synchronized (lock) {
            for (Iterator<FilesTask> iterator = waitingFilesTask.iterator(); iterator.hasNext(); ) {
                FilesTask filesTask = iterator.next();
                if (processingFilesTask.size() >= poolSize) break;
                iterator.remove();
                executableTaskList.add(filesTask);
                processingFilesTask.add(filesTask);
            }
            for (FilesTask filesTask : executableTaskList) {
                nowExecutedTask++;
                executeFilesTaskInExecutor(filesTask);
            }
        }

    }

    private synchronized ExecutorService getExecutorService() {
        if (executorService == null || executorService.isShutdown()) {
            //创建一个线程数固定的线程池
            executorService = Executors.newFixedThreadPool(poolSize);
        }
        if (!decodeTask.isActive()) {
            decodeTask.execute();
        }
        return executorService;
    }

    private void shutdownExecutor() {
        getExecutorService().shutdown();
    }

    private void executeFilesTaskInExecutor(FilesTask filesTask) {
        getExecutorService().execute(filesTask);
    }

    public static class Builder {
        private int poolSize;
        private int maxTask;
        private ResultCallBack resultCallBack;
        private Processor dataProcessor;

        public Builder() {
            poolSize = 8;//默认线程数
            maxTask = 30;//最大执行任务数，当执行完毕后关闭线程池
        }

        public Dispatcher build() {
            return new Dispatcher(this);
        }

        public Builder setPoolSize(int poolSize) {
            this.poolSize = poolSize;
            return this;
        }

        public Builder setMaxTask(int maxTask) {
            this.maxTask = maxTask;
            return this;
        }

        public Builder setResultCallBack(ResultCallBack resultCallBack) {
            this.resultCallBack = resultCallBack;
            return this;
        }

        public Builder setDataProcessor(Processor processor) {
            this.dataProcessor = processor;
            return this;
        }
    }

}
