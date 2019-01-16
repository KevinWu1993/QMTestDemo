package qmutil.task;

import qmutil.Config;
import qmutil.bean.CompatibleBean;
import qmutil.bean.Data;
import qmutil.bean.EncodeData;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;

/**
 * author: KevinWu
 * date: 2019/1/16
 * description: 数据解析Task，将FileReadingTask读取出来的字符串数据解码为Data数据结构
 */

public class DataDecodeTask extends Task {
    private RealDecodeTask realDecodeTask;

    @Override
    public void process(ExecutorService executorService, Object lock, CompatibleBean compatibleBean) {
        if (lock != null && compatibleBean instanceof EncodeData) {
            EncodeData encodeData = (EncodeData) compatibleBean;
            getRealDecodeTask(lock).addData(encodeData.getData());
        }
    }

    public void finish() {
        realDecodeTask.finish();
    }

    private RealDecodeTask getRealDecodeTask(Object lock) {
        synchronized (lock) {
            if (realDecodeTask == null) {
                realDecodeTask = new RealDecodeTask(lock, new TaskEvent() {
                    @Override
                    public void onDecode(Data data) {
                        nextTask.process(null, lock, data);
                    }

                    @Override
                    public void onFinish() {
                        nextTask.finish();
                    }
                });
                realDecodeTask.execute();
            }
        }
        return realDecodeTask;
    }

    private static class RealDecodeTask implements Runnable {
        private final Deque<String> dataBeanStringCache = new ArrayDeque<>();//缓存队列
        private final Object lock;
        private boolean isFinish = false;
        private final TaskEvent taskEvent;

        RealDecodeTask(Object lock, TaskEvent taskEvent) {
            this.lock = lock;
            this.taskEvent = taskEvent;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (lock) {
                    while (dataBeanStringCache.size() == 0 && !isFinish) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //消费掉数据
                    Iterator<String> iterator = dataBeanStringCache.iterator();
                    while (iterator.hasNext()) {
                        String line = iterator.next();
                        iterator.remove();
                        Data data = readFileLineAsDataBean(line);
                        if (data != null) {
                            taskEvent.onDecode(data);
                        }
                    }
                    if (isFinish) {
                        break;
                    }
                    lock.notifyAll();
                }
            }
            //数据消费完毕
            taskEvent.onFinish();

        }

        private void finish() {
            synchronized (lock) {
                isFinish = true;
                lock.notifyAll();
            }
        }

        private void execute() {
            Thread thisTask = new Thread(this);
            thisTask.start();
        }

        private void addData(String s) {
            synchronized (lock) {
                dataBeanStringCache.add(s);
                lock.notifyAll();
            }
        }

        /**
         * 读取一行文件，恢复DataBean结构
         *
         * @param line 一行数据
         * @return 返回解析后的DataBean结构
         */
        private Data readFileLineAsDataBean(String line) {
            String[] oneLine = line.split(Config.ROW_SEPARATOR);
            if (oneLine.length == 3)
                return new Data(oneLine[0], oneLine[1], Float.parseFloat(oneLine[2]));
            else
                return null;
        }

    }

    private interface TaskEvent {
        void onDecode(Data data);

        void onFinish();
    }
}
