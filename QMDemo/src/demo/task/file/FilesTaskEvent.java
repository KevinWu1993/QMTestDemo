package demo.task.file;


/**
 * author: KevinWu
 * date: 2019/1/15
 * description: 文件读取任务
 */

public interface FilesTaskEvent {

    void onFileReadResult(String result);

    void onFileReadException(FilesTask task, String msg);

    void onFileReadFinish(FilesTask task);
}
