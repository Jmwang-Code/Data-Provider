package com.cn.jmw.data.provider.test;

import com.cn.jmw.data.provider.base.OuterDataSourceManager;
import com.cn.jmw.data.provider.base.bean.DataProviderSource;
import com.cn.jmw.data.provider.base.bean.common.DataSourceTypeEnum;
import com.cn.jmw.data.provider.base.factory.DataProviderAbstractFactory;
import com.cn.jmw.data.provider.base.response.ResponseBody;
import org.junit.Test;

import java.util.HashMap;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年10月09日 14:50
 * @Version 1.0
 */
public class OuterDataSourceManagerTest {
    static OuterDataSourceManager outerDataSourceManager = new OuterDataSourceManager();

    static HashMap map = new HashMap();
    static {
        map.put("dbType","MYSQL");
        map.put("driverClass","com.mysql.cj.jdbc.Driver");
        map.put("url","jdbc:mysql://152.136.154.249:3306/demo?&allowMultiQueries=true&characterEncoding=utf-8");
        map.put("user","demo");
        map.put("password","kKJ8XynXLzjfYDA7");
    }

    static DataProviderSource source = new DataProviderSource().builder()
            .type("JDBC")
            .name("DEMO")
            .sourceId("1")
            .properties(map)
            .build();

    @Test
    public void loadDataProviderFactories(){
        outerDataSourceManager.loadDataProviderFactories();
        DataProviderAbstractFactory jdbc = outerDataSourceManager.cachedDataProviderAbstractFactories.get(DataSourceTypeEnum.JDBC.name());
        System.out.println(jdbc.getConfigJsonFileName());
    }

    @Test
    public void getDataProviderFactory(){
        DataProviderAbstractFactory jdbc = outerDataSourceManager.getDataProviderFactory(DataSourceTypeEnum.JDBC.name());
        System.out.println(jdbc.getConfigJsonFileName());
    }

    @Test
    public void testConnection() throws Exception {
        ResponseBody responseBody = outerDataSourceManager.testConnection(source);
        System.out.println(responseBody);
    }
}
