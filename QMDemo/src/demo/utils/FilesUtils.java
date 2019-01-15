package demo.utils;

import demo.DemoConfig;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

/**
 * author: KevinWu
 * date: 2019/1/14
 * description: 文件处理相关工具
 */

public class FilesUtils {

    /**
     * 获取夹所有txt文件
     *
     * @param path 文件夹路径
     * @return 文件List
     */
    public static List<String> getFilePathList(String path) {
        if (path == null) return null;
        File fileDir = new File(path);
        if (!fileDir.exists() || !fileDir.isDirectory()) return null;
        FilenameFilter filenameFilter = (dir, name) -> name != null && name.endsWith(DemoConfig.FILE_SUFFIX);
        String[] files = fileDir.list(filenameFilter);
        if (files != null && files.length > 0)
            return Arrays.asList(files);
        else
            return null;
    }
}
