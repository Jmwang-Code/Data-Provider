package com.cn.jmw.data.provider.jdbc;

import com.cn.jmw.data.provider.base.factory.DataProviderAbstractFactory;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年10月08日 18:23
 * @Version 1.0
 */
public class JdbcProvider extends DataProviderAbstractFactory {

    public final String CONFIG = "JDBC-CONFIG.json";

    @Override
    public String getConfigJsonFile() {
        return CONFIG;
    }
}
