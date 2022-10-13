package com.cn.jmw.data.provider.base;

import com.cn.jmw.data.provider.base.entity.DataSourceProviderEntity;
import com.cn.jmw.data.provider.base.factory.DataProviderAbstractFactory;
import com.cn.jmw.data.provider.base.response.ResponseBody;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author jmw
 * @Description Provider模式顶级管理者 —— 对外提供数据源管理者
 * @date 2022年10月08日 18:02
 * @Version 1.0
 */
@Slf4j
public class OuterDataSourceManager extends DataSourceExecuteOptimizer implements DataSourceManager {

    /**
     * @Description 跳表数据结构的 缓冲池
     */
    public Map<String, DataProviderAbstractFactory> cachedDataProviderAbstractFactories = new ConcurrentSkipListMap<>();

    @Override
    public ResponseBody testConnection(DataSourceProviderEntity source) throws Exception {
        //获取服务
        return ResponseBody
                .builder()
                .status(getDataProvider(source.getType()).test(source))
                .build();
    }

    /**
     * @Author jmw
     * @Description 获取具体工厂
     * @Date 18:38 2022/10/8
     */
    public DataProviderAbstractFactory getDataProvider(String type) {
        if (cachedDataProviderAbstractFactories.size() == 0) {
            loadDataProviders();
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
     * @Date 18:38 2022/10/8
     */
    public void loadDataProviders() {
        //预览版本API 默认下禁用
        //Thread.startVirtualThread(() -> {
        //Code to execute in virtual thread
        System.out.println("Virtual Thread");
        ServiceLoader<DataProviderAbstractFactory> load = ServiceLoader.load(DataProviderAbstractFactory.class);
        for (DataProviderAbstractFactory dataProvider : load) {
            try {
                //已经进行了数据加载
                cachedDataProviderAbstractFactories.put(dataProvider.getType(), dataProvider);
            } catch (IOException e) {
                log.error("", e);
            }
        }
        //});
    }

}
