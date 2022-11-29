package com.cn.jmw.data.provider.builder;


import com.cn.jmw.data.provider.ThreadLocalCache;
import com.cn.jmw.data.provider.base.entity.db.Dataframe;
import com.cn.jmw.data.provider.builder.chain.Builder;
import com.cn.jmw.data.provider.builder.en.Page;
import com.cn.jmw.data.provider.builder.en.Query;
import com.cn.jmw.data.provider.builder.en.Sort;
import com.cn.jmw.data.provider.builder.plugins.plu.Analysis;
import com.cn.jmw.data.provider.es.entity.EsRequestParam;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.builder.SearchSourceBuilder;


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

    public Dataframe reData(){
        Dataframe dataframe = null;
        ThreadLocalCache threadLocalCache = new ThreadLocalCache()
                .put(Plugin.ES_REQUEST_PARAM, this.esRequestParam)
                .put(Plugin.SEARCH_REQUEST, this.searchRequest)
                .put(Plugin.SEARCH_SOURCE_BUILDER, this.searchSourceBuilder)
                .build();
        ElasticSearchBuilder elasticSearchBuilder = new ElasticSearchBuilder(threadLocalCache);
        SearchResponse build = elasticSearchBuilder
                .setPage(Page.SCROLL_PAGE)
                .setSort(Sort.ASC)
                .setQuery(Query.INDEX_ALL)
                .build();
        Analysis.append(threadLocalCache);
        dataframe = (Dataframe)threadLocalCache.get(Plugin.DATA_FRAME);
        threadLocalCache.close();
        return dataframe;
    }

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
                .sort("ASC")
                .sortName("customer_phone")
                .query("day_of_week")
                .value(new String[]{"Saturday"})
                .build());
        ThreadLocalCache threadLocalCache = new ThreadLocalCache()
                .put(Plugin.ES_REQUEST_PARAM, builderPatternManager.esRequestParam)
                .put(Plugin.SEARCH_REQUEST, builderPatternManager.searchRequest)
                .put(Plugin.SEARCH_SOURCE_BUILDER, builderPatternManager.searchSourceBuilder)
                .build();
        ElasticSearchBuilder elasticSearchBuilder = new ElasticSearchBuilder(threadLocalCache);
        SearchResponse build = elasticSearchBuilder
                .setPage(Page.SCROLL_PAGE)
                .setSort(Sort.ASC)
                .setQuery(Query.INDEX_ALL)
                .build();
        Analysis.append(threadLocalCache);
        Dataframe dataframe = (Dataframe)threadLocalCache.get(Plugin.DATA_FRAME);
        threadLocalCache.close();
        System.out.println(dataframe);
    }

}
