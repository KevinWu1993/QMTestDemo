package demo.dataprocessor;

import java.util.List;

/**
 * author: KevinWu
 * date: 2019/1/15
 * description: 数据处理接口
 */

public interface Processor<M> {
    /**
     * 插入数据到处理器
     * @param m M类型的数据
     */
    void putData(M m);

    /**
     * 获取数据处理结果
     * @return List<M>类型结果返回，返回所有数据
     */
    List<M> getProcessedDataList();

    /**
     * 获取数据处理结果
     * @param size 获取结果的个数
     * @return List<M>类型结果返回，返回size个数据
     */
    List<M> getProcessedDataList(int size);
}
