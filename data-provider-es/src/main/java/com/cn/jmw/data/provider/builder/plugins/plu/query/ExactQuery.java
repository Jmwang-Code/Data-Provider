package com.cn.jmw.data.provider.builder.plugins.plu.query;

import com.cn.jmw.data.provider.ThreadLocalCache;
import com.cn.jmw.data.provider.builder.Plugin;
import com.cn.jmw.data.provider.builder.plugins.plu.QueryPlugin;
import com.cn.jmw.data.provider.es.entity.EsRequestParam;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月21日 16:57
 * @Version 1.0
 */
public class ExactQuery extends QueryPlugin {

    private EsRequestParam esRequestParam;

    private SearchSourceBuilder searchSourceBuilder;

    private SearchRequest searchRequest;

    private SearchResponse searchResponse;

    private String index;

    private String query;

    private String[] value;

    public ExactQuery(ThreadLocalCache threadLocalCache) {
        esRequestParam = (EsRequestParam) threadLocalCache.get(Plugin.ES_REQUEST_PARAM);
        searchSourceBuilder = (SearchSourceBuilder) threadLocalCache.get(Plugin.SEARCH_SOURCE_BUILDER);
        searchRequest = (SearchRequest) threadLocalCache.get(Plugin.SEARCH_REQUEST);
        searchResponse = (SearchResponse) threadLocalCache.get(Plugin.SEARCH_RESPONSE);
        threadLocalCache.put(Plugin.SEARCH_RESPONSE,searchResponse);
        this.index = esRequestParam.getIndex();
        this.query = esRequestParam.getQuery();
        this.value = esRequestParam.getValue();
    }


    @Override
    public void append() {
        // 构建查询条件（注意：termsQuery 支持多种格式查询，如 boolean、int、double、string 等，这里使用的是 string 的查询）
        searchSourceBuilder.query(QueryBuilders.termsQuery(query, value));
        // 创建查询请求对象，将查询对象配置到其中
        searchRequest.indices(index);
        searchRequest.source(searchSourceBuilder);
        // 执行查询，然后处理响应结果
        try {
            searchResponse = esRequestParam.getRestHighLevelClient().search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
