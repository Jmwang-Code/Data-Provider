package com.cn.jmw.data.provider.jdbc;

import com.cn.jmw.data.provider.base.entity.DataSourceProviderEntity;
import com.cn.jmw.data.provider.base.entity.JdbcProperties;
import com.cn.jmw.data.provider.base.entity.db.Dataframe;
import com.cn.jmw.data.provider.base.entity.db.ExecutionParam;
import com.cn.jmw.data.provider.base.factory.DataProviderAbstractFactory;
import com.cn.jmw.data.provider.jdbc.adapter.JdbcDataProviderAdapter;
import com.cn.jmw.data.provider.jdbc.factory.AdapterFactory;
import com.cn.jmw.data.provider.jdbc.factory.DataSourceConnectionPoolFactory;
import com.cn.jmw.data.provider.jdbc.factory.DataSourceConnectionPoolDruid;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年10月08日 18:23
 * @Version 1.0
 */
public class JdbcProvider extends DataProviderAbstractFactory {

    public Map<String, JdbcDataProviderAdapter> cacheAdapters = new ConcurrentSkipListMap();

    public final String CONFIG = "JDBC-CONFIG.json";

    public static final String DB_TYPE = "dbType";

    public static final String USER = "user";

    public static final String PASSWORD = "password";

    public static final String URL = "url";

    public static final String DRIVER_CLASS = "driverClass";

    public static final String ENABLE_SPECIAL_SQL = "enableSpecialSQL";

    /**
     * 获取连接时最大等待时间（毫秒）
     */
    public static final Integer DEFAULT_MAX_WAIT = 5000;

    /**
     * @Description Match or insert adapter
     * 
     * 
     * 
     * @Author jmw
     * @Date 16:05 2022/10/21
     */
    public JdbcDataProviderAdapter matchOrInsertAdapter(DataSourceProviderEntity source){
        //如果有就匹配返回对象
        JdbcDataProviderAdapter adapter = cacheAdapters.get(source.getSourceId());
        if (adapter!=null)return adapter;

        //如果没有就创建初始化对象
        adapter = AdapterFactory.createDataAdapter(conv2JdbcProperties(source), true);
        cacheAdapters.put(source.getSourceId(),adapter);
        return adapter;
    }

    /**
     * @Description DataSourceProviderEntity object converted to JdbcProperties object
     *
     *
     *
     * @Author jmw
     * @Date 16:06 2022/10/21
     */
    public JdbcProperties conv2JdbcProperties(DataSourceProviderEntity source){
        JdbcProperties jdbcProperties = new JdbcProperties();
        //做验证器
        jdbcProperties.setDbType(source.getProperties().get(DB_TYPE).toString().toUpperCase());
        jdbcProperties.setUrl(source.getProperties().get(URL).toString());
        Object user = source.getProperties().get(USER);
        if (user != null && StringUtils.isNotBlank(user.toString())) {
            jdbcProperties.setUser(user.toString());
        }
        Object password = source.getProperties().get(PASSWORD);
        if (password != null && StringUtils.isNotBlank(password.toString())) {
            jdbcProperties.setPassword(password.toString());
        }
        String driverClass = source.getProperties().getOrDefault(DRIVER_CLASS, "").toString();
        jdbcProperties.setDriverClass(StringUtils.isBlank(driverClass) ?
                AdapterFactory.getJdbcDriverInfo(jdbcProperties.getDbType()).getDriverClass() :
                driverClass);

        Object enableSpecialSQL = source.getProperties().get(ENABLE_SPECIAL_SQL);

        if (enableSpecialSQL != null && "true".equals(enableSpecialSQL.toString())) {
            jdbcProperties.setEnableSpecialSql(true);
        }

        Object properties = source.getProperties().get("properties");
        if (properties != null) {
            if (properties instanceof Map) {
                Properties prop = new Properties();
                prop.putAll((Map) properties);
                jdbcProperties.setProperties(prop);
            }
        }
        return jdbcProperties;
    }

    /**
     * @Description Get data source connection pool
     * 
     * 
     * 
     * @Author jmw
     * @Date 16:10 2022/10/21
     */
    public static DataSourceConnectionPoolFactory<? extends DataSource> getDataSourceConnectionPool() {
        return new DataSourceConnectionPoolDruid();
    }

    @Override
    public Object test(DataSourceProviderEntity source) throws Exception {
        //对象提取转换
        JdbcProperties jdbcProperties = conv2JdbcProperties(source);
        return AdapterFactory.createDataAdapter(jdbcProperties,false).test(jdbcProperties);
    }

    @Override
    public Dataframe execute(DataSourceProviderEntity source, ExecutionParam executionParam) throws SQLException {
        JdbcDataProviderAdapter adapter = matchOrInsertAdapter(source);

        //Assume that data aggregation or normal execution may occur
        Dataframe dataframe = adapter.executionOnSource(executionParam);
        return dataframe;
    }

    @Override
    public String getConfigJsonFileName() {
        return CONFIG;
    }
}
