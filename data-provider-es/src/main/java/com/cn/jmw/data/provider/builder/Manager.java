package com.cn.jmw.data.provider.builder;


import com.cn.jmw.data.provider.ThreadLocalCache;
import com.cn.jmw.data.provider.es.entity.EsRequestParam;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import static com.cn.jmw.data.provider.builder.Plugin.*;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月09日 17:39
 * @Version 1.0
 */
public class Manager {

    private SearchRequest searchRequest;

    private SearchSourceBuilder searchSourceBuilder;

    public Manager(EsRequestParam esRequestParam) {
        searchRequest = new SearchRequest();
        searchSourceBuilder = new SearchSourceBuilder();
        this.esRequestParam = esRequestParam;
    }

    private EsRequestParam esRequestParam;

    /**
     * objectObjectHashMap.put(" name ", " testOne ");
     * objectObjectHashMap.put("address", "152.136.154.249:9200");
     * objectObjectHashMap.put("scheme", "http");
     * objectObjectHashMap.put("query", "");
     * objectObjectHashMap.put("username", "");
     * objectObjectHashMap.put("password", "");
     * objectObjectHashMap.put("index", "kibana_sample_data_ecommerce");
     * objectObjectHashMap.put("model", "indexId");//index-id
     * objectObjectHashMap.put("id", "-43_74MBeofnJbrlVzm1");
     */
    public static void main(String[] args) {
        Manager builderPatternManager = new Manager(EsRequestParam.builder()
                .size(3)
                .pageStart(0)
                .address("152.136.154.249:9200")
                .schema("http")
                .index("kibana_sample_data_ecommerce")
                .id("43_74MBeofnJbrlVzm1")
                .username("default")
                .build());
        ThreadLocalCache threadLocalCache = new ThreadLocalCache()
                .put(ES_REQUEST_PARAM, builderPatternManager.esRequestParam)
                .put(SEARCH_REQUEST, builderPatternManager.searchRequest)
                .put(SEARCH_SOURCE_BUILDER, builderPatternManager.searchSourceBuilder);
        ElasticSearchBuilder elasticSearchBuilder = new ElasticSearchBuilder(threadLocalCache);
        ElasticSearchPluginManager vegMeal = elasticSearchBuilder.prepareVegMeal();
        vegMeal.showItems();
        threadLocalCache.close();
    }

}

enum Plugin {
    ES_REQUEST_PARAM,
    SEARCH_REQUEST,
    SEARCH_SOURCE_BUILDER;
}