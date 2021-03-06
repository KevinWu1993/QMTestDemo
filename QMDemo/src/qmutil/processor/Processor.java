package qmutil.processor;

import qmutil.bean.Data;

import java.util.List;

/**
 * author: KevinWu
 * date: 2019/1/15
 * description: 抽象Data类型数据处理接口
 */

public interface Processor {
    /**
     * 插入数据到处理器
     * @param data M类型的数据
     */
    void putData(Data data);

    /**
     * 获取数据处理结果
     * @return List<Data>类型结果返回，返回所有数据
     */
    List<Data> getProcessedDataList();

    /**
     * 获取数据处理结果
     * @param size 获取结果的个数
     * @return List<Data>类型结果返回，返回size个数据，
     * 当分组很大或者允许分组存在重复数据时，数据量会很大，
     * 这时可以使用这个方法按需返回前面几位的数据
     */
    List<Data> getProcessedDataList(int size);
}
