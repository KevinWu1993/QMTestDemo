package demo.task.decode;

import demo.DemoConfig;
import demo.callback.ResultCallBack;
import demo.dataprocessor.Processor;
import demo.entity.DataBean;
import demo.task.AbsTask;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * author: KevinWu
 * date: 2019/1/15
 * description: 数据解析任务
 */

public class DecodeTask extends AbsTask {
    private final Deque<String> dataBeanStringCache = new ArrayDeque<>();//缓存队列
    private ResultCallBack resultCallBack;
    private Processor processor;
    private boolean isFinish = false;
    private boolean isActive = false;
    private final Object lock;

    public DecodeTask(Object lock, Processor processor, ResultCallBack resultCallBack) {
        this.lock = lock;
        this.processor = processor;
        this.resultCallBack = resultCallBack;
    }

    public void finish() {
        isFinish = true;
        isActive = false;
    }

    @Override
    public void execute() {
        Thread thisTask = new Thread(this);
        thisTask.start();
        isActive = true;
    }

    public boolean isActive() {
        return isActive;
    }

    public void add(String data) {
        synchronized (lock) {
            dataBeanStringCache.add(data);
            lock.notifyAll();
        }
    }

    @Override
    public void run() {
        while (!isFinish) {
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
                    DataBean dataBean = readFileLineAsDataBean(line);
                    if (dataBean != null) {
                        processor.putData(dataBean);
                    }
                }
                lock.notifyAll();
            }
        }
        resultCallBack.onResult(processor.getProcessedDataList());
    }

    /**
     * 读取一行文件，恢复DataBean结构
     *
     * @param line 一行数据
     * @return 返回解析后的DataBean结构
     */
    private DataBean readFileLineAsDataBean(String line) {
        String[] oneLine = line.split(DemoConfig.ROW_SEPARATOR);
        if (oneLine.length == 3)
            return new DataBean(oneLine[0], oneLine[1], Float.parseFloat(oneLine[2]));
        else
            return null;
    }
}
