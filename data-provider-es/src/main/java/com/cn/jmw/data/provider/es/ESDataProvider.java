package com.cn.jmw.data.provider.es;

import com.cn.jmw.data.provider.base.OuterDataSourceManager;
import com.cn.jmw.data.provider.base.entity.DataSourceProviderEntity;
import com.cn.jmw.data.provider.base.entity.db.Dataframe;
import com.cn.jmw.data.provider.base.entity.db.Dataframes;
import com.cn.jmw.data.provider.base.entity.db.ExecutionParam;
import com.cn.jmw.data.provider.base.entity.db.UUIDGenerator;
import com.cn.jmw.data.provider.base.factory.DataProviderAbstractDefaultFactory;
import com.cn.jmw.data.provider.es.elasticsearch.EsInfoProcessor;
import com.cn.jmw.data.provider.es.elasticsearch.Mode;
import com.cn.jmw.data.provider.es.entity.EsRequestParam;
import com.cn.jmw.data.provider.es.entity.ResponseData;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年10月18日 16:16
 * @Version 1.0
 */
public class ESDataProvider extends DataProviderAbstractDefaultFactory {

    private static final String I18N_PREFIX = "config.template.es.";

//    @Override
//    public String getConfigDisplayName(String name) {
//        return MessageResolver.getMessage(I18N_PREFIX + name);
//    }
//
//    @Override
//    public String getConfigDescription(String name) {
//        String message = MessageResolver.getMessage(I18N_PREFIX + name + ".desc");
//        if (message.startsWith(I18N_PREFIX)) {
//            return null;
//        } else {
//            return message;
//        }
//    }

//    @Override
//    public String getQueryKey(DataSourceProviderEntity config, ExecutionParam executeParam) throws Exception {
//
//    }


    @Override
    public String getConfigJsonFileName() {
        return "ES-CONFIG.json";
    }

    //加载数据源
    @Override
    public Dataframes fullLoadOfDataSource(DataSourceProviderEntity config) throws Exception {
        //转换请求参数
        List<EsRequestParam> esRequestParams = convertRequestParams(config);
        if (CollectionUtils.isEmpty(esRequestParams)) {
            return Dataframes.of(config.getSourceId());
        }
        //获取MD5key
        String dataKey = DigestUtils.md5Hex(esRequestParams.stream()
                .map(EsRequestParam::toCacheKey)
                .collect(Collectors.joining(",")));

        Dataframes dataframes = Dataframes.of(dataKey);

        for (EsRequestParam esRequestParam : esRequestParams) {
            Dataframe dataframe = new EsInfoProcessor(esRequestParam).fetchAndParse();
            dataframe.setName(esRequestParam.getName()+ UUIDGenerator.generate());
            dataframes.add(dataframe);
        }
        return dataframes;
    }

    private List<EsRequestParam> convertRequestParams(DataSourceProviderEntity source) throws ClassNotFoundException {
        List<EsRequestParam> esRequestParams = new ArrayList<>();
        Map<String, Object> properties = source.getProperties();
        esRequestParams.add(EsRequestParam.builder()
                .name((String) properties.get("name"))
                .address((String) properties.get("address"))
                .query((String) properties.get("query"))
                .schema((String) properties.get("scheme"))
                .username((String) properties.get("username"))
                .password((String) properties.get("password"))
                .index((String) properties.get("index"))
                .mode(Mode.getModel((String) properties.get("model")))
                .id((String) properties.get("id"))
                .build());
        return esRequestParams;
    }

    @Test
    public void set() throws Exception {
        OuterDataSourceManager providerManager = new OuterDataSourceManager();
        Map<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("name", "testOne");
        objectObjectHashMap.put("address", "152.136.154.249:9200");
        objectObjectHashMap.put("scheme", "http");
        objectObjectHashMap.put("query", "");
        objectObjectHashMap.put("username", "");
        objectObjectHashMap.put("password", "");
        objectObjectHashMap.put("index", "kibana_sample_data_ecommerce");
        objectObjectHashMap.put("model", "indexId");//index-id
        objectObjectHashMap.put("id", "-43_74MBeofnJbrlVzm1");
        System.out.println(ResponseData.success(providerManager.testConnection(DataSourceProviderEntity
                .builder()
                .sourceId("asdad")
                .type("ES")
                .name("ES")
                .properties(objectObjectHashMap)
                .build())));
    }

}
