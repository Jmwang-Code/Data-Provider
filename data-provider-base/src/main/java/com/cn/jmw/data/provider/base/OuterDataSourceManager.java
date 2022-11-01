package com.cn.jmw.data.provider.base;

import com.cn.jmw.data.provider.base.entity.DataSourceProviderEntity;
import com.cn.jmw.data.provider.base.entity.db.ExecutionParam;
import com.cn.jmw.data.provider.base.factory.DataProviderAbstractFactory;
import com.cn.jmw.data.provider.base.response.ResponseBody;
import com.cn.jmw.data.provider.base.response.ResponseData;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.SQLException;
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
    public ResponseData testConnection(DataSourceProviderEntity source) throws Exception {
        //获取服务
        return ResponseData
                .builder()
                .data(getDataProvider(source.getType()).test(source))
                .build();
    }

    @Override
    public ResponseBody execute(DataSourceProviderEntity source, ExecutionParam executionParam) throws Exception {
        /**
         * TODO
         * 1.响应预处理
         * 2.获取提供者服务
         * 3.调用提供者服务
         * TODO 4.缓存机制
         * 5.并发优化
         */

        return ResponseBody
                .builder()
                .status(ResponseBody.jsonConverter(getDataProvider(source.getType()).execute(source,executionParam)))
                .build();
    }


    /**
     * @Author jmw
     * @Description 获取具体工厂
     * @Date 18:38 2022/10/8
     */
    public DataProviderAbstractFactory getDataProvider(String type) {
        DataProviderAbstractFactory dataProvider = cachedDataProviderAbstractFactories.size() == 0
                ? loadDataProviders().cachedDataProviderAbstractFactories.get(type)
                : cachedDataProviderAbstractFactories.get(type);
        if (dataProvider == null) log.error("No data provider type " + type);
        return dataProvider;
    }

    /**
     * @Author jmw
     * @Description 加载工厂
     * @Date 18:38 2022/10/8
     */
    public OuterDataSourceManager loadDataProviders() {
        for (DataProviderAbstractFactory dataProvider : ServiceLoader.load(DataProviderAbstractFactory.class)) {
            try {
                cachedDataProviderAbstractFactories.put(dataProvider.getType(), dataProvider);
            } catch (IOException e) {
                log.error("", e);
            }
        }
        return this;
    }

}
