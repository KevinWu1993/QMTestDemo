package qmutil.processor;

import qmutil.bean.Data;

import java.util.*;

/**
 * author: KevinWu
 * date: 2019/1/15
 * description: Data抽象处理器，用于将Data数据规整为Map<String,Set<Data>>形式抽象，其中String为分组id
 */

public abstract class AbsDataBeanProcessor implements Processor {
    final Map<String,Set<Data>> dataSetMap = newMap();

    protected abstract List<Data> processDataBeanSet(List<Data> processedList, Set<Data> dataSet);

    protected abstract Map<String,Set<Data>> newMap();

    @Override
    public List<Data> getProcessedDataList() {
        return getProcessedDataList(dataSetMap.size());
    }

    @Override
    public List<Data> getProcessedDataList(int size) {
        if(size<=0 || size > dataSetMap.size()){
            return null;
        } else {
            List<Data> processedList = new ArrayList<>();
            Iterator<Map.Entry<String, Set<Data>>> it = dataSetMap.entrySet().iterator();
            for(;it.hasNext() && size>0; size--) {
                Map.Entry<String, Set<Data>> entry = it.next();
                Set<Data> dataSet = entry.getValue();
                processedList = processDataBeanSet(processedList, dataSet);
            }
            return processedList;
        }
    }
}
