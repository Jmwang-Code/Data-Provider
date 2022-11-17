package com.cn.jmw.data.provider.builder;

import com.alibaba.fastjson.JSON;
import com.cn.jmw.data.provider.builder.plugins.PagePlugin;
import com.cn.jmw.data.provider.builder.plugins.Plugins;
import com.cn.jmw.data.provider.builder.plugins.plu.page.FromSizePage;
import com.cn.jmw.data.provider.es.entity.EsRequestParam;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Before;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月09日 17:39
 * @Version 1.0
 */
public class BuilderPatternDemo {
    /**
     *  objectObjectHashMap.put(" name ", " testOne ");
     *         objectObjectHashMap.put("address", "152.136.154.249:9200");
     *         objectObjectHashMap.put("scheme", "http");
     *         objectObjectHashMap.put("query", "");
     *         objectObjectHashMap.put("username", "");
     *         objectObjectHashMap.put("password", "");
     *         objectObjectHashMap.put("index", "kibana_sample_data_ecommerce");
     *         objectObjectHashMap.put("model", "indexId");//index-id
     *         objectObjectHashMap.put("id", "-43_74MBeofnJbrlVzm1");
     */
    public static void main(String[] args) {
        EsRequestParam esRequestParam = EsRequestParam.builder()
                .size(3)
                .pageStart(0)
                .address("152.136.154.249:9200")
                .schema("http")
                .index("kibana_sample_data_ecommerce")
                .id("43_74MBeofnJbrlVzm1")
                .username("default")
                .build();


        ElasticSearchBuilder elasticSearchBuilder = new ElasticSearchBuilder(esRequestParam);
        ElasticSearchPluginManager vegMeal = elasticSearchBuilder.prepareVegMeal();
        vegMeal.showItems();
    }
}