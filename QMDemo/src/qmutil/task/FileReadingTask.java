package qmutil.task;

import qmutil.bean.CompatibleBean;
import qmutil.bean.EncodeData;
import qmutil.bean.Request;

import java.io.*;
import java.util.concurrent.ExecutorService;

public class FileReadingTask extends Task {

    @Override
    public void process(ExecutorService executorService, Object lock, CompatibleBean compatibleBean) {
        RealFileTask realFileTask = new RealFileTask((Request) compatibleBean, new FileTaskEvent() {
            @Override
            public void onFileReadResult(String result) {
                nextTask.process(null, lock, new EncodeData(result));
            }

            @Override
            public void onFileReadFinish(Request request) {
                if (callBack != null) {
                    callBack.onResult(request);
                }
            }
        });
        executorService.execute(realFileTask);
    }

    @Override
    public void finish() {
        //文件读取task启动后不提供停止方法，
    }

    private static class RealFileTask implements Runnable {
        private final Request request;
        private final FileTaskEvent fileTaskEvent;

        RealFileTask(Request request, FileTaskEvent fileTaskEvent) {
            this.request = request;
            this.fileTaskEvent = fileTaskEvent;
        }


        @Override
        public void run() {
            if (fileTaskEvent == null || request == null) return;
            InputStreamReader reader = null;
            BufferedReader br = null;
            try {
                reader = new InputStreamReader(new FileInputStream(request.getFile()));
                br = new BufferedReader(reader);
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.equals("")) {
                        fileTaskEvent.onFileReadResult(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                    if (reader != null) {
                        reader.close();
                    }
                    fileTaskEvent.onFileReadFinish(request);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    interface FileTaskEvent {

        void onFileReadResult(String result);

        void onFileReadFinish(Request request);
    }
}
