package qmutil.bean;

import java.io.File;

public class Request implements CompatibleBean{
    private File file;

    public Request(File file) {
        this.file = file;
    }
    public File getFile() {
        return file;
    }



}
