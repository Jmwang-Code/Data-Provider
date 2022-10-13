package com.cn.jmw.data.provider.jdbc.factory;

import com.cn.jmw.data.provider.base.entity.JdbcProperties;

import javax.sql.DataSource;

/**
 * @author jmw
 * @Description 数据源连接池简单工厂
 * @date 2022年10月08日 18:24
 * @Version 1.0
 */
public interface DataSourceConnectionPoolFactory<T extends DataSource> {

    T createDataSource(JdbcProperties jdbcProperties) throws Exception;

    void destroy(DataSource dataSource);
}
