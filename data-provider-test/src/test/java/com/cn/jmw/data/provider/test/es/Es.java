package com.cn.jmw.data.provider.test.es;

import com.cn.jmw.data.provider.base.OuterDataSourceManager;
import com.cn.jmw.data.provider.base.entity.DataSourceProviderEntity;
import com.cn.jmw.data.provider.base.response.ResponseData;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年10月25日 19:48
 * @Version 1.0
 */
public class Es {

    static OuterDataSourceManager outerDataSourceManager = new OuterDataSourceManager();


    @Test
    public void set() throws Exception {
        Map<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("host", "152.136.154.249");
        objectObjectHashMap.put("port", "9200");
        objectObjectHashMap.put("scheme", "http");
        objectObjectHashMap.put("query", "");
        objectObjectHashMap.put("username", "");
        objectObjectHashMap.put("password", "");
        objectObjectHashMap.put("index", "kibana_sample_data_ecommerce");
        objectObjectHashMap.put("model", "index_id");//index-id
        objectObjectHashMap.put("id", "-43_74MBeofnJbrlVzm1");
        System.out.println(ResponseData.success(outerDataSourceManager.testConnection(DataSourceProviderEntity
                .builder()
                .sourceId("asdad")
                .type("ES")
                .name("ES")
                .properties(objectObjectHashMap)
                .build())));
    }
}
