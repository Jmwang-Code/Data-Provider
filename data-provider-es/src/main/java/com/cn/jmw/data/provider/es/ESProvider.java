package com.cn.jmw.data.provider.es;

import com.cn.jmw.data.provider.base.entity.DataSourceProviderEntity;
import com.cn.jmw.data.provider.base.entity.db.Dataframes;
import com.cn.jmw.data.provider.base.factory.DataProviderAbstractDefaultFactory;
import com.cn.jmw.data.provider.base.factory.DataProviderAbstractFactory;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年10月17日 18:08
 * @Version 1.0
 */
public class ESProvider extends DataProviderAbstractDefaultFactory {

    private final String CONFIG = "ES-CONFIG.json";

    @Override
    public String getConfigJsonFileName() {
        return CONFIG;
    }

    @Override
    public Dataframes fullLoadOfDataSource(DataSourceProviderEntity config) throws Exception {
        return null;
    }
}
