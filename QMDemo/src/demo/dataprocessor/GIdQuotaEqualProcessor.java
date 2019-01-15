package demo.dataprocessor;

import demo.entity.DataBean;

import java.util.*;

/**
 * author: KevinWu
 * date: 2019/1/15
 * description: GIdQuotaEqualProcessor处理器返回同组的quota相同的所有元素
 */

public class GIdQuotaEqualProcessor extends AbsQuotaCompareProcessor {

    @Override
    protected List<DataBean> processDataBeanSet(List<DataBean> processedList, Set<DataBean> dataBeanSet) {
        float tempQuota = -1;
        for (DataBean dataBean : dataBeanSet) {
            if (tempQuota != -1 && dataBean.getQuota() != tempQuota) break;
            tempQuota = dataBean.getQuota();
            processedList.add(dataBean);
        }
        return processedList;
    }
}
