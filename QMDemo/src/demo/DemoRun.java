package demo;

import demo.callback.ResultCallBack;
import demo.dataprocessor.GIdQuotaUniqueProcessor;
import demo.entity.DataBean;
import demo.task.file.FilesTask;
import demo.utils.FilesUtils;

import java.io.File;
import java.util.List;

/**
 * author: KevinWu
 * date: 2019/1/14
 * description: 测试类
 */

public class DemoRun {

    public static void main(String[] args) {
        List<String> filesList = FilesUtils.getFilePathList(DemoConfig.BASE_PATH);
        if (filesList != null && filesList.size() > 0) {
            Dispatcher dispatcher = new Dispatcher.Builder()
                    .setMaxTask(filesList.size())//设置最大任务数为文件个数
                    .setPoolSize(8)//设置线程池大小
                    .setDataProcessor(new GIdQuotaUniqueProcessor())//设置具体处理器
                    .setResultCallBack((ResultCallBack<List<DataBean>>) dataBeans -> {
                        for (DataBean dataBean : dataBeans) {
                            System.out.println(dataBean.getGroupId()
                                    + ", " + dataBean.getId()
                                    + ", " + dataBean.getQuota());
                        }
                    })
                    .build();
            for (String aFilesList : filesList) {
                try {
                    //入队文件
                    dispatcher.enqueueTask(new FilesTask(new File(DemoConfig.BASE_PATH + aFilesList)));
                } catch (IllegalAccessException e) {
                    //任务执行数超出设置的最大值会抛出这个异常
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("未找到可读取文件，请先使用utils/MakeFilesUtils生成测试文件");
        }

    }
}
