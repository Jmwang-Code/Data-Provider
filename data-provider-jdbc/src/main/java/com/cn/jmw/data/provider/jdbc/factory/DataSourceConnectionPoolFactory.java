package com.cn.jmw.data.provider.jdbc.factory;

import com.cn.jmw.data.provider.base.entity.JdbcProperties;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author jmw
 * @Description 数据源连接池简单工厂
 * @date 2022年10月08日 18:24
 * @Version 1.0
 */
public interface DataSourceConnectionPoolFactory<T extends DataSource> {

    /**
     * @Description Create the connection pool of the data source
     * 
     * 
     * 
     * @Author jmw
     * @Date 17:28 2022/10/20
     */
    T createDataSource(JdbcProperties jdbcProperties) throws Exception;

    /**
     * @Description Destroy the connection pool of the data source
     * 
     * 
     * 
     * @Author jmw
     * @Date 17:29 2022/10/20
     */
    void destroy(DataSource dataSource);

    /**
     * @Description Initialize the connection pool configuration of data source
     * 
     * 
     * 
     * @Author jmw
     * @Date 17:48 2022/10/20
     */
    Properties configDataSource(JdbcProperties properties);
}
