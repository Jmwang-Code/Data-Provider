package com.cn.jmw.data.provider.base;

import com.cn.jmw.data.provider.base.factory.DataProviderAbstractFactory;
import com.cn.jmw.data.provider.base.response.ResponseBody;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author jmw
 * @Description 对外提供数据源管理者
 * @date 2022年10月08日 18:02
 * @Version 1.0
 */
@Slf4j
public class OuterDataSourceManager extends DataSourceExecuteOptimizer implements DataSourceManager{

    /**
     * @Description 跳表数据结构的 缓冲池
     */
    private Map<String, DataProviderAbstractFactory> cachedDataProviderAbstractFactories = new ConcurrentSkipListMap<>();

    @Override
    public <T> ResponseBody testConnection(T t) {
        //获取服务
        return ResponseBody.builder().body(getDataProviderFactory((String) t).toString()).build();
    }

    /**
     * @Author jmw
     * @Description 获取具体工厂
     * @param type: 
     * @return DataProviderAbstractFactory
     * @Date 18:38 2022/10/8
     */
    private DataProviderAbstractFactory getDataProviderFactory(String type) {
        if (cachedDataProviderAbstractFactories.size() == 0) {
            loadDataProviderFactories();
        }
        DataProviderAbstractFactory dataProvider = cachedDataProviderAbstractFactories.get(type);
        if (dataProvider == null) {
            log.info("No data provider type " + type);
        }
        return dataProvider;
    }

    /**
     * @Author jmw
     * @Description 加载工厂
     * @return void
     * @Date 18:38 2022/10/8
     */
    void loadDataProviderFactories() {
        ServiceLoader<DataProviderAbstractFactory> load = ServiceLoader.load(DataProviderAbstractFactory.class);
        for (DataProviderAbstractFactory dataProvider : load) {
            try {
                cachedDataProviderAbstractFactories.put(dataProvider.getType(), dataProvider);
            } catch (IOException e) {
                log.error("", e);
            }
        }
    }

}
