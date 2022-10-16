package com.cn.jmw.data.provider.jdbc.adapter;

import com.cn.jmw.data.provider.base.entity.JdbcDriverInfo;
import com.cn.jmw.data.provider.base.entity.JdbcProperties;
import com.cn.jmw.data.provider.base.utils.EntityValidatorUtil;
import com.cn.jmw.data.provider.jdbc.JdbcProvider;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * @author jmw
 * @Description TODO
 * @date 2022年10月08日 18:25
 * @Version 1.0
 */
@Slf4j
public class JdbcDataProviderAdapter implements Closeable {

    private JdbcProperties jdbcProperties;
    private JdbcDriverInfo driverInfo;
    private DataSource dataSource;
    protected boolean init;

    /**
     * @Author jmw
     * @Description 连接池只初始化一次
     * @Date 12:18 2022/10/13
     */
    public final void init(JdbcProperties jdbcProperties, JdbcDriverInfo driverInfo) {
        try {
            this.jdbcProperties = jdbcProperties;
            this.driverInfo = driverInfo;
            this.dataSource = JdbcProvider.getDataSourceConnectionPool().createDataSource(jdbcProperties);
        } catch (Exception e) {
            log.error("data provider init error", e);
        }
        this.init = true;
    }

    /**
     * @Author jmw
     * @Description 销毁
     * @Date 18:29 2022/10/8
     */
    @Override
    public void close() throws IOException {
        System.out.println("shutdown");
    }

    public boolean test(JdbcProperties jdbcProperties){
        //数据验证 注释的约束进行验证（NotBlank）
        EntityValidatorUtil.validate(jdbcProperties);
        try{
            Class.forName(jdbcProperties.getDriverClass());
        } catch (ClassNotFoundException e) {
            String errMsg = "Driver class not found " + jdbcProperties.getDriverClass();
            log.error(errMsg, e);
        }
        try {
            //jdk原生方法 进行链接数据库测试，不报错为链接成功
            DriverManager.getConnection(jdbcProperties.getUrl(), jdbcProperties.getUser(), jdbcProperties.getPassword());
        } catch (SQLException sqlException) {
            log.error(sqlException.getSQLState()+"\n"+sqlException.getMessage());
        }
        return true;
    }
}
