package qmutil.task;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import qmutil.bean.CompatibleBean;
import qmutil.callback.CallBack;

import java.util.concurrent.ExecutorService;

/**
 * author: KevinWu
 * date: 2019/1/16
 * description: 以责任链模式抽象Task，
 *              注意这里不一定每个Task都会实现finish方法，
 *              如FileReadingTask生命周期是在线程池中控制的，无需手动finish
 */

public abstract class Task {
    protected Task nextTask;
    protected CallBack callBack;

    public abstract void process(@Nullable ExecutorService executorService,
                                 @NotNull Object lock,
                                 @NotNull CompatibleBean compatibleBean);

    public abstract void finish();

    public void setNextTask(Task task) {
        this.nextTask = task;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
}
