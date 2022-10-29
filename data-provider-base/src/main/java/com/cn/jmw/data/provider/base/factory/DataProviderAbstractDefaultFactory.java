package com.cn.jmw.data.provider.base.factory;

import com.cn.jmw.data.provider.base.entity.DataSourceProviderEntity;
import com.cn.jmw.data.provider.base.entity.db.Dataframe;
import com.cn.jmw.data.provider.base.entity.db.Dataframes;
import com.cn.jmw.data.provider.base.entity.db.ExecutionParam;
import com.cn.jmw.data.provider.base.local.LocalPersistentDB;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;

@Slf4j
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
        ExecutionParam executeParam = ExecutionParam.empty();
        return execute(source,executeParam);
    }

    /**
     * Generally,the default factory is inherited,the execute method executes and persisted
     */
    @Override
    public Dataframe execute(DataSourceProviderEntity source, ExecutionParam executionParam) throws Exception {
        //加载数据源
        Dataframes dataframes = fullLoadOfDataSource(source);

        boolean persistent = isCacheEnabled(source);
        Date expire = null;
        if (persistent) {
            expire = getExpireTime(source);
        }

        //持久化
        return LocalPersistentDB.executeLocalQuery(executionParam, dataframes, persistent, expire);

    }

    protected boolean isCacheEnabled(DataSourceProviderEntity config) {
        try {
            return (boolean) config.getProperties().getOrDefault("cacheEnable", false);
        } catch (Exception e) {
            return false;
        }
    }

    protected Date getExpireTime(DataSourceProviderEntity config) {
        Object cacheTimeout = config.getProperties().get("cacheTimeout");
        if (cacheTimeout == null) {
            log.info("cache timeout can not be empty");
        }
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE, Integer.parseInt(cacheTimeout.toString()));
        return instance.getTime();
    }
}
