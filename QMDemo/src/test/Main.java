package test;

import qmutil.Config;
import qmutil.QMDemoClient;
import qmutil.bean.Data;
import qmutil.bean.Request;
import qmutil.processor.GIdQuotaUniqueProcessor;
import qmutil.utils.FilesUtils;

import java.io.File;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<String> filesList = FilesUtils.getFilePathList(Config.BASE_PATH);
        if(filesList != null && filesList.size() > 0) {
            QMDemoClient qmDemoClient = new QMDemoClient.Builder()
                    .setMaxRequest(filesList.size())
                    .setPoolSize(8)
                    .setProcessor(new GIdQuotaUniqueProcessor())
                    .setResultCallBack(data -> {
                        for (Data dataBean : data) {
                            System.out.println(dataBean.getGroupId()
                                    + ", " + dataBean.getId()
                                    + ", " + dataBean.getQuota());
                        }
                    }).build();
            for(String filePath : filesList) {
                qmDemoClient.enqueueRequest(new Request(new File(Config.BASE_PATH + filePath)));
            }
        } else {
            System.out.println("请先使用MakeFilesUtils生成测试文件");
        }
    }
}
