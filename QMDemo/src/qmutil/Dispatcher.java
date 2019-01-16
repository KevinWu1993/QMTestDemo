package qmutil;

import qmutil.bean.Data;
import qmutil.callback.CallBack;
import qmutil.bean.Request;
import qmutil.processor.Processor;
import qmutil.task.DataDecodeTask;
import qmutil.task.DataProcessTask;
import qmutil.task.FileReadingTask;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author: KevinWu
 * date: 2019/1/14
 * description: 任务分发器，用于协调线程池、等待请求队列、正在执行请求队列
 */

final class Dispatcher {
    private final int poolSize;//默认线程数
    private final int maxTask;//最大执行任务数，当执行完毕后关闭线程池
    private int nowExecutedTask = 0;
    private int addTask = 0;
    private int nowFinishTask = 0;
    private ExecutorService executorService;
    private final Deque<Request> waitingRequest = new ArrayDeque<>();
    private final Deque<Request> processingRequest = new ArrayDeque<>();
    private final DataDecodeTask dataDecodeTask;
    private final Object lock = new Object();

    private Dispatcher(Builder builder) {
        this.poolSize = builder.poolSize;
        this.maxTask = builder.maxTask;
        DataProcessTask dataProcessTask = new DataProcessTask(builder.processor);
        dataProcessTask.setCallBack(builder.resultCallBack);
        dataDecodeTask = new DataDecodeTask();
        dataDecodeTask.setNextTask(dataProcessTask);
    }

    public void enqueueRequest(Request request) {
        synchronized (lock) {
            addTask++;
            if (addTask <= maxTask) {
                waitingRequest.add(request);
            }
        }
        processRequest();
    }

    private void processRequest() {
        List<Request> processableRequest = new ArrayList<>();
        synchronized (lock) {
            for (Iterator<Request> iterator = waitingRequest.iterator(); iterator.hasNext(); ) {
                Request request = iterator.next();
                if (processingRequest.size() >= poolSize) break;
                iterator.remove();
                processableRequest.add(request);
                processingRequest.add(request);
            }
            for (Request request : processableRequest) {
                nowExecutedTask++;
                //创建FileReadingTask
                FileReadingTask fileReadingTask = new FileReadingTask();
                fileReadingTask.setNextTask(dataDecodeTask);
                fileReadingTask.setCallBack(new CallBack<Request>() {
                    @Override
                    public void onResult(Request request) {
                        synchronized (lock) {
                            nowFinishTask ++;
                            processingRequest.remove(request);
                            if (nowExecutedTask >= maxTask && nowFinishTask == nowExecutedTask) {
                                //当所有任务执行完，关闭线程池和decodeTask
                                shutdownExecutor();
                                dataDecodeTask.finish();
                            }
                            processRequest();
                        }
                    }
                });
                fileReadingTask.process(getExecutorService(), lock, request);
            }
        }

    }

    private synchronized ExecutorService getExecutorService() {
        if (executorService == null || executorService.isShutdown()) {
            //创建一个线程数固定的线程池
            executorService = Executors.newFixedThreadPool(poolSize);
        }
        return executorService;
    }

    private void shutdownExecutor() {
        getExecutorService().shutdown();
    }

    public static class Builder {
        private int poolSize;
        private int maxTask;
        private CallBack<List<Data>> resultCallBack;
        private Processor processor;

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

        public Builder setResultCallBack(CallBack<List<Data>> resultCallBack) {
            this.resultCallBack = resultCallBack;
            return this;
        }

        public Builder setProcessor(Processor processor) {
            this.processor = processor;
            return this;
        }
    }

}
