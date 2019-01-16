package qmutil.processor;

import qmutil.bean.Data;

import java.util.List;
import java.util.Set;

/**
 * author: KevinWu
 * date: 2019/1/15
 * description: GIdQuotaUniqueProcessor处理器只会返回同组的第一个元素，忽略quota相同的情况
 */

public class GIdQuotaUniqueProcessor extends AbsQuotaCompareProcessor {

    @Override
    protected List<Data> processDataBeanSet(List<Data> processedList, Set<Data> dataSet) {
        for (Data data : dataSet) {
            processedList.add(data);
            break;
        }
        return processedList;
    }
}
