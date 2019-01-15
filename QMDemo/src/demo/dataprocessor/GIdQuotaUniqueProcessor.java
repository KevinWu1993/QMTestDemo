package demo.dataprocessor;

import demo.entity.DataBean;

import java.util.*;

/**
 * author: KevinWu
 * date: 2019/1/15
 * description: GIdQuotaUniqueProcessor处理器只会返回同组的第一个元素，忽略quota相同的情况
 */

public class GIdQuotaUniqueProcessor extends AbsQuotaCompareProcessor {

    @Override
    protected List<DataBean> processDataBeanSet(List<DataBean> processedList, Set<DataBean> dataBeanSet) {
        for (DataBean dataBean : dataBeanSet) {
            processedList.add(dataBean);
            break;
        }
        return processedList;
    }
}
