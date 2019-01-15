package demo.dataprocessor;

import demo.entity.DataBean;

import java.util.*;

/**
 * author: KevinWu
 * date: 2019/1/15
 * description: 这个处理器用来处理按Quota升序排序的元素
 */

public abstract class AbsQuotaCompareProcessor extends AbsDataBeanProcessor {

    @Override
    protected Map<String, Set<DataBean>> newMap() {
        return new TreeMap<>(Comparator.naturalOrder());
    }

    /**
     * 插入数据
     *
     * @param dataBean 待插入数据
     */
    @Override
    public void putData(DataBean dataBean) {
        //TreeMap实现了Comparator，这里直接插入，无需处理排序
        Set<DataBean> set = dataSetMap.get(dataBean.getGroupId());
        if (set != null) {
            set.add(dataBean);
        } else {
            set = new TreeSet<>((o1, o2) -> {
                if (o1.getQuota() - o2.getQuota() == 0) return 0;
                else return o1.getQuota() - o2.getQuota() > 0 ? 1 : -1;
            });
            set.add(dataBean);
            dataSetMap.put(dataBean.getGroupId(), set);
        }
    }
}
