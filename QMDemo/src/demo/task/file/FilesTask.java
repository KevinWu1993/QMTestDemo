package demo.task.file;

import demo.task.AbsTask;
import demo.task.decode.DecodeTask;

import java.io.*;

/**
 * author: KevinWu
 * date: 2019/1/14
 * description: 文件处理task
 */

public class FilesTask extends AbsTask {
    private final File file;
    private FilesTaskEvent filesTaskEvent;

    public FilesTask(File file) {
        this.file = file;
    }

    public void setFilesTaskEvent(FilesTaskEvent filesTaskEvent) {
        this.filesTaskEvent = filesTaskEvent;
    }

    @Override
    protected void execute() {
        if (filesTaskEvent == null || file == null) return;
        InputStreamReader reader = null;
        BufferedReader br = null;
        try {
            reader = new InputStreamReader(new FileInputStream(file));
            br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                if(!line.equals("")) {
                    filesTaskEvent.onFileReadResult(line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            filesTaskEvent.onFileReadException(this, "FileNotFoundException异常");
        } catch (IOException e) {
            e.printStackTrace();
            filesTaskEvent.onFileReadException(this, "IOException异常");
        } finally {
            try {
                if (br != null)
                    br.close();
                if (reader != null)
                    reader.close();
                filesTaskEvent.onFileReadFinish(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
