package demo.dataprocessor;

import demo.entity.DataBean;

import java.util.*;

/**
 * author: KevinWu
 * date: 2019/1/15
 * description: DataBase抽象处理器
 */

public abstract class AbsDataBeanProcessor implements Processor<DataBean>{
    final Map<String,Set<DataBean>> dataSetMap = newMap();

    protected abstract List<DataBean> processDataBeanSet(List<DataBean> processedList, Set<DataBean> dataBeanSet);

    protected abstract Map<String,Set<DataBean>> newMap();

    @Override
    public List<DataBean> getProcessedDataList() {
        return getProcessedDataList(dataSetMap.size());
    }

    @Override
    public List<DataBean> getProcessedDataList(int size) {
        if(size<=0 || size > dataSetMap.size()){
            return null;
        } else {
            List<DataBean> processedList = new ArrayList<>();
            Iterator<Map.Entry<String, Set<DataBean>>> it = dataSetMap.entrySet().iterator();
            for(;it.hasNext() && size>0; size--) {
                Map.Entry<String, Set<DataBean>> entry = it.next();
                Set<DataBean> dataBeanSet = entry.getValue();
                processedList = processDataBeanSet(processedList, dataBeanSet);
            }
            return processedList;
        }
    }
}
