package com.cn.jmw.data.provider.base.factory;

import com.cn.jmw.data.provider.base.entity.DataSourceProviderEntity;
import com.cn.jmw.data.provider.base.entity.PageInfo;
import com.cn.jmw.data.provider.base.entity.db.Dataframe;
import com.cn.jmw.data.provider.base.entity.db.Dataframes;
import com.cn.jmw.data.provider.base.entity.db.ExecutionParam;

/**
 * @author jmw
 * @Description 默认工厂
 * @date 2022年10月17日 18:15
 * @Version 1.0
 */
public abstract class DataProviderAbstractDefaultFactory extends DataProviderAbstractFactory{

//    public static final String TEST_DATA_SIZE = "size";

    /**
     * 从数据源加载全量数据
     *
     * @param config 数据源配置
     */
    public abstract Dataframes fullLoadOfDataSource(DataSourceProviderEntity config) throws Exception;

    /**
     * If the default factory is inherited,the test method tests and executes
     */
    @Override
    public Object test(DataSourceProviderEntity source) throws Exception {
        return null;
    }

    /**
     * Generally,the default factory is inherited,the execute method executes and persisted
     */
    @Override
    public Dataframe execute(DataSourceProviderEntity source, ExecutionParam executionParam) {
        return null;
    }
}
