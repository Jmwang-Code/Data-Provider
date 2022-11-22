package com.cn.jmw.data.provider.builder.plugins.plu.query;

import com.cn.jmw.data.provider.ThreadLocalCache;
import com.cn.jmw.data.provider.builder.Plugin;
import com.cn.jmw.data.provider.builder.plugins.plu.QueryPlugin;
import com.cn.jmw.data.provider.es.entity.EsRequestParam;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月22日 13:58
 * @Version 1.0
 */
public class MatchAllQuery extends QueryPlugin {


    private EsRequestParam esRequestParam;

    private SearchSourceBuilder searchSourceBuilder;

    private SearchRequest searchRequest;


    public MatchAllQuery(ThreadLocalCache threadLocalCache){
        esRequestParam = (EsRequestParam) threadLocalCache.get(Plugin.ES_REQUEST_PARAM);
        searchSourceBuilder = (SearchSourceBuilder) threadLocalCache.get(Plugin.SEARCH_SOURCE_BUILDER);
        searchRequest = (SearchRequest) threadLocalCache.get(Plugin.SEARCH_REQUEST);
    }

    @Override
    public void append(ThreadLocalCache threadLocalCache) {
        // 创建查询请求对象，将查询对象配置到其中
        searchRequest.indices(esRequestParam.getIndex());
        searchRequest.source(searchSourceBuilder.query(QueryBuilders.matchAllQuery()));
        try {
            threadLocalCache.put(Plugin.SEARCH_RESPONSE,esRequestParam.getRestHighLevelClient().search(searchRequest, RequestOptions.DEFAULT));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
