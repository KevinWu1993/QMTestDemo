package demo.task;

/**
 * author: KevinWu
 * date: 2019/1/14
 * description: 抽象任务定义
 */

public abstract class AbsTask implements Runnable {

    @Override
    public void run() {
        execute();
    }

    protected abstract void execute();
}
