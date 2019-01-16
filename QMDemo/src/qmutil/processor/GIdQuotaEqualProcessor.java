package qmutil.processor;

import qmutil.bean.Data;

import java.util.List;
import java.util.Set;

/**
 * author: KevinWu
 * date: 2019/1/15
 * description: GIdQuotaEqualProcessor处理器返回同组的quota相同的所有元素
 */

public class GIdQuotaEqualProcessor extends AbsQuotaCompareProcessor {

    @Override
    protected List<Data> processDataBeanSet(List<Data> processedList, Set<Data> dataSet) {
        float tempQuota = -1;//quota为大于等于0的数据为前提
        for (Data data : dataSet) {
            if (tempQuota != -1 && data.getQuota() != tempQuota) break;
            tempQuota = data.getQuota();
            processedList.add(data);
        }
        return processedList;
    }
}
