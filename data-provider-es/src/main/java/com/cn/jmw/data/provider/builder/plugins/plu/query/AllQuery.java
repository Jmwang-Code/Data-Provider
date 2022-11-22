package com.cn.jmw.data.provider.builder.plugins.plu.query;

import com.cn.jmw.data.provider.ThreadLocalCache;
import com.cn.jmw.data.provider.builder.Plugin;
import com.cn.jmw.data.provider.builder.plugins.plu.QueryPlugin;
import com.cn.jmw.data.provider.es.entity.EsRequestParam;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月21日 21:41
 * @Version 1.0
 */
public class AllQuery extends QueryPlugin {

    private EsRequestParam esRequestParam;

    private SearchSourceBuilder searchSourceBuilder;

    private SearchRequest searchRequest;


    public AllQuery(ThreadLocalCache threadLocalCache){
        esRequestParam = (EsRequestParam) threadLocalCache.get(Plugin.ES_REQUEST_PARAM);
        searchSourceBuilder = (SearchSourceBuilder) threadLocalCache.get(Plugin.SEARCH_SOURCE_BUILDER);
        searchRequest = (SearchRequest) threadLocalCache.get(Plugin.SEARCH_REQUEST);
    }

    @Override
    public void append(ThreadLocalCache threadLocalCache) {
        searchRequest.indices(esRequestParam.getIndex());
        searchRequest.source(new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()));
        try {
            threadLocalCache.put(Plugin.SEARCH_RESPONSE,esRequestParam.getRestHighLevelClient().search(searchRequest, RequestOptions.DEFAULT));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
