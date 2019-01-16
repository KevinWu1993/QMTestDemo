package qmutil.bean;

import java.io.File;

/**
 * author: KevinWu
 * date: 2019/1/16
 * description: 一种请求的封装，对外暴露，用户在使用QMUtil进行文件处理时需要构建一个Request
 */

public class Request implements CompatibleBean {
    private File file;

    public Request(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }


}
