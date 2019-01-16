package qmutil.task;


import com.sun.istack.internal.NotNull;
import qmutil.bean.CompatibleBean;
import qmutil.bean.Data;
import qmutil.processor.Processor;

import java.util.concurrent.ExecutorService;

/**
 * 字符串数据处理为结果所需数据结构
 */
public class DataProcessTask extends Task {
    private final Processor processor;

    public DataProcessTask(@NotNull Processor processor) {
        this.processor = processor;
    }

    @Override
    public void process(ExecutorService executorService, Object lock, CompatibleBean compatibleBean) {
        processor.putData((Data) compatibleBean);
    }

    @Override
    public void finish() {
        callBack.onResult(processor.getProcessedDataList());
    }
}
