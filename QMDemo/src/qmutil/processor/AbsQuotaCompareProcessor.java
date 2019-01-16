package qmutil.processor;

import qmutil.bean.Data;

import java.util.*;

/**
 * author: KevinWu
 * date: 2019/1/15
 * description: 这个处理器用来处理按Quota升序排序的元素，将在插入的时候对数据进行排序
 */

public abstract class AbsQuotaCompareProcessor extends AbsDataBeanProcessor {

    @Override
    protected Map<String, Set<Data>> newMap() {
        return new TreeMap<>(Comparator.naturalOrder());
    }

    /**
     * 插入数据
     *
     * @param data 待插入数据
     */
    @Override
    public void putData(Data data) {
        Set<Data> set = dataSetMap.get(data.getGroupId());
        if (set != null) {
            set.add(data);
        } else {
            set = new TreeSet<>((o1, o2) -> {
                if (o1.getQuota() - o2.getQuota() == 0) return 0;
                else return o1.getQuota() - o2.getQuota() > 0 ? 1 : -1;
            });
            set.add(data);
            dataSetMap.put(data.getGroupId(), set);
        }
    }
}
