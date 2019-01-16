package qmutil;

import qmutil.bean.Data;
import qmutil.bean.Request;
import qmutil.callback.CallBack;
import qmutil.processor.GIdQuotaUniqueProcessor;
import qmutil.processor.Processor;

import java.util.List;

public class QMDemoClient {
    private final Dispatcher dispatcher;

    public QMDemoClient(CallBack<List<Data>> resultCallBack) {
        this(new Builder().setResultCallBack(resultCallBack));
    }

    public void enqueueRequest(Request request) {
        dispatcher.enqueueRequest(request);
    }


    private QMDemoClient(Builder builder){
        dispatcher = new Dispatcher.Builder()
                .setPoolSize(builder.poolSize)
                .setMaxTask(builder.maxRequest)
                .setResultCallBack(builder.resultCallBack)
                .setProcessor(builder.processor)
                .build();
    }

    public static class Builder {
        private int poolSize;//默认线程数
        private int maxRequest;//最大执行请求
        private CallBack<List<Data>> resultCallBack;
        private Processor processor;

        public Builder() {
            poolSize = 8;//默认线程数
            maxRequest = 30;//最大执行任务数，当执行完毕后关闭线程池
            processor = new GIdQuotaUniqueProcessor();//默认数据处理器
        }

        public QMDemoClient build() {
            return new QMDemoClient(this);
        }

        public Builder setPoolSize(int poolSize) {
            this.poolSize = poolSize;
            return this;
        }

        public Builder setMaxRequest(int maxRequest) {
            this.maxRequest = maxRequest;
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
