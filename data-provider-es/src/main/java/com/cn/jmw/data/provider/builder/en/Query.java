package com.cn.jmw.data.provider.builder.en;

import com.cn.jmw.data.provider.es.entity.EsRequestParam;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月29日 18:06
 * @Version 1.0
 */
public enum Query {

    INDEX_ALL,

    INDEX_ID,

    TERM_QUERY,

    MATCH_ALL_QUERY,

    FUZZY_QUERY,

    RANGE_QUERY,

    WILDCARD_QUERY
}
