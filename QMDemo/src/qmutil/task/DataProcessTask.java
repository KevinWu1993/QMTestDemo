package qmutil.task;


import com.sun.istack.internal.NotNull;
import qmutil.bean.CompatibleBean;
import qmutil.bean.Data;
import qmutil.processor.Processor;

import java.util.concurrent.ExecutorService;

/**
 * author: KevinWu
 * date: 2019/1/16
 * description: 数据处理规整Task，将Data数据结构集中放到Processor中进行处理
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
