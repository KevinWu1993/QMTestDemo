package demo.utils;

import demo.DemoConfig;

import java.io.*;
import java.util.Random;

/**
 * author: KevinWu
 * date: 2019/1/14
 * description: 此类用于产生测试文件，执行此类main方法即可，如果需要修改生成个数，后缀等可以在DemoConfig中修改
 */

public class MakeFilesUtils {

    private static final int[] GROUP_ID = new int[]{
            100, 101, 102, 103,
            104, 105, 106, 107,
            108, 109, 110, 111,
            112, 113, 114, 115,
            116, 117, 118, 119};//groupId取值范围
    private static int ID_START = 1;//id字段开始值，添加一项就自增，保证不重复


    public static void main(String[] args) {
        //创建文件夹
        File dir = new File(DemoConfig.BASE_PATH);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdir();
        }
        System.out.println("开始产生测试文件");
        for (int i = 0; i < DemoConfig.FILE_COUNT; i++) {
            System.out.print("产生测试文件：");
            PrintWriter writer = null;
            try {
                //创建文件
                String fileName = String.valueOf(System.currentTimeMillis()) + i + DemoConfig.FILE_SUFFIX;
                File writeName = new File(DemoConfig.BASE_PATH + fileName);
                writeName.createNewFile();
                writer = new PrintWriter(DemoConfig.BASE_PATH + fileName, DemoConfig.CHARSET);
                //一个文件中随机写入300~2000组数据
                int recordCount = getRandomInt(300, 2000);
                while (recordCount-- > 0) {
                    StringBuffer sb = new StringBuffer();
                    sb.append(ID_START++);
                    sb.append(DemoConfig.ROW_SEPARATOR);
                    sb.append(GROUP_ID[getRandomInt(0, GROUP_ID.length - 1)]);
                    sb.append(DemoConfig.ROW_SEPARATOR);
                    //quota数据产生随机浮点数据，150.0f以内
                    sb.append(getRandomFloat(0f, 150f));
                    writer.println(sb.toString());
                }
                System.out.println(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
    }

    /**
     * 获取[start,end]范围内的随机整数
     *
     * @param start 随机数开始范围
     * @param end   随机数结束范围
     * @return 返回随机数
     */
    private static int getRandomInt(int start, int end) {
        Random random = new Random();
        return random.nextInt(end + 1 - start) + start;
    }

    /**
     * 获取[start,end]范围内的随机浮点数
     *
     * @param start 随机数开始范围
     * @param end   随机数结束范围
     * @return 返回随机数
     */
    private static float getRandomFloat(float start, float end) {
        Random random = new Random();
        return (end - start) * random.nextFloat() + start;
    }
}