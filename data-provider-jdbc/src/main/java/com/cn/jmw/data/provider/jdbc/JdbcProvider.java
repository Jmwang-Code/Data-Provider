package com.cn.jmw.data.provider.jdbc;

import com.cn.jmw.data.provider.base.bean.DataProviderSource;
import com.cn.jmw.data.provider.base.bean.JdbcProperties;
import com.cn.jmw.data.provider.base.factory.DataProviderAbstractFactory;
import com.cn.jmw.data.provider.jdbc.adapter.JdbcDataProviderAdapter;
import com.cn.jmw.data.provider.jdbc.factory.AdapterFactory;

import java.util.Map;
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

        return jdbcProperties;
    }

    @Override
    public Object test(DataProviderSource source) throws Exception {
        return AdapterFactory.createDataAdapter(conv2JdbcProperties(source),false).test();
    }

    @Override
    public String getConfigJsonFileName() {
        return CONFIG;
    }
}
