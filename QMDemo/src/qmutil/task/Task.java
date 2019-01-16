package qmutil.task;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import qmutil.bean.CompatibleBean;
import qmutil.callback.CallBack;

import java.util.concurrent.ExecutorService;

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
