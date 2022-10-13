package com.cn.jmw.data.provider.jdbc.factory;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.cn.jmw.data.provider.base.bean.JdbcProperties;
import com.cn.jmw.data.provider.jdbc.JdbcProvider;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年10月08日 18:25
 * @Version 1.0
 */
@Slf4j
public class DataProviderAdapterFactoryDruid implements DataProviderAdapterFactory{

    @Override
    public DruidDataSource createDataSource(JdbcProperties jdbcProperties) throws Exception {
        Properties properties = configDataSource(jdbcProperties);
        DruidDataSource druidDataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
        druidDataSource.setBreakAfterAcquireFailure(true);
        druidDataSource.setConnectionErrorRetryAttempts(0);
        log.info("druid data source created ({})", druidDataSource.getName());
        return druidDataSource;
    }

    @Override
    public void destroy(DataSource dataSource) {

    }

    /**
     * @Author jmw
     * @Description 获取德鲁伊连接池中的配置数据
     * @Date 12:22 2022/10/13
     */
    private Properties configDataSource(JdbcProperties properties) {
        Properties pro = new Properties();
        //connect params
        pro.setProperty(DruidDataSourceFactory.PROP_DRIVERCLASSNAME, properties.getDriverClass());
        pro.setProperty(DruidDataSourceFactory.PROP_URL, properties.getUrl());
        if (properties.getUser() != null) {
            pro.setProperty(DruidDataSourceFactory.PROP_USERNAME, properties.getUser());
        }
        if (properties.getPassword() != null) {
            pro.setProperty(DruidDataSourceFactory.PROP_PASSWORD, properties.getPassword());
        }
        pro.setProperty(DruidDataSourceFactory.PROP_MAXWAIT, JdbcProvider.DEFAULT_MAX_WAIT.toString());

        System.setProperty("druid.mysql.usePingMethod", "false");

        //opt config
        pro.putAll(properties.getProperties());
        return pro;
    }
}
