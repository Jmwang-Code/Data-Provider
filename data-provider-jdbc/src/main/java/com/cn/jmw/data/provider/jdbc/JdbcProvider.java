package com.cn.jmw.data.provider.jdbc;

import com.cn.jmw.data.provider.base.bean.DataProviderSource;
import com.cn.jmw.data.provider.base.bean.JdbcDriverInfo;
import com.cn.jmw.data.provider.base.bean.JdbcProperties;
import com.cn.jmw.data.provider.base.factory.DataProviderAbstractFactory;
import com.cn.jmw.data.provider.jdbc.adapter.JdbcDataProviderAdapter;
import com.cn.jmw.data.provider.jdbc.factory.AdapterFactory;
import com.cn.jmw.data.provider.jdbc.factory.DataProviderAdapterFactory;
import com.cn.jmw.data.provider.jdbc.factory.DataProviderAdapterFactoryDruid;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
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
     * @Author jmw
     * @Description 匹配或者插入缓冲池中适配器
     * @Date 16:25 2022/10/9
     */
    public JdbcDataProviderAdapter matchOrInsertAdapter(DataProviderSource source){
        //如果有就匹配返回对象
        JdbcDataProviderAdapter adapter = cacheAdapters.get(source.getSourceId());
        if (adapter!=null)return adapter;

        //如果没有就创建初始化对象
        adapter = AdapterFactory.createDataAdapter(conv2JdbcProperties(source), true);
        cacheAdapters.put(source.getSourceId(),adapter);
        return adapter;
    }

    /**
     * @Author jmw
     * @Description DataProviderSource 对象转换为 JdbcProperties对象
     * @Date 16:39 2022/10/9
     */
    public JdbcProperties conv2JdbcProperties(DataProviderSource source){
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
     * @Author jmw
     * @Description 获取连接池
     * @Date 12:17 2022/10/13
     */
    public static DataProviderAdapterFactory<? extends DataSource> getDataSourceFactory() {
        return new DataProviderAdapterFactoryDruid();
    }

    @Override
    public Object test(DataProviderSource source) throws Exception {
        //对象提取转换
        JdbcProperties jdbcProperties = conv2JdbcProperties(source);
        return AdapterFactory.createDataAdapter(jdbcProperties,false).test(jdbcProperties);
    }

    @Override
    public String getConfigJsonFileName() {
        return CONFIG;
    }
}
