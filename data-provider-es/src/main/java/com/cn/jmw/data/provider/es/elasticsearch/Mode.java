package com.cn.jmw.data.provider.es.elasticsearch;

import com.cn.jmw.data.provider.es.elasticsearch.research.PolymerizationReturn;
import com.cn.jmw.data.provider.es.elasticsearch.research.Return;
import com.cn.jmw.data.provider.es.elasticsearch.research.SearchReturn;
import com.cn.jmw.data.provider.es.entity.EsRequestParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月07日 14:32
 * @Version 1.0
 */
public enum Mode implements ModeInter {
    /**
     * 精确查询 termQuery
     * 匹配查询 matchAllQuery
     * 模糊查询 fuzzyQuery
     * 范围查询 rangeQuery
     * 通配符查询 wildcardQuery
     * boolean查询 boolQuery
     */
    INDEX_ALL("indexAll") {
        @Override
        public SearchResponse initReturn(EsRequestParam esRequestParam) {
            SearchRequest request = new SearchRequest(esRequestParam.getIndex());
            request.source(new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()));
            SearchResponse searchResponse = null;
            try {
                searchResponse = esRequestParam.getRestHighLevelClient().search(request, RequestOptions.DEFAULT);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return searchResponse;
        }
    },

    INDEX_ID("indexId") {
        @Override
        public SearchResponse initReturn(EsRequestParam esRequestParam) {
            SearchRequest request = new SearchRequest(esRequestParam.getIndex());
            request.source(new SearchSourceBuilder().query(QueryBuilders.termQuery("_id", esRequestParam.getId())));
            SearchResponse searchResponse = null;
            try {
                searchResponse = esRequestParam.getRestHighLevelClient().search(request, RequestOptions.DEFAULT);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return searchResponse;
        }
    },

    //TODO
    TERM_QUERY("termQuery") {
        @Override
        public SearchResponse initReturn(EsRequestParam esRequestParam) {
            SearchResponse searchResponse = null;
            try {
                // 构建查询条件（注意：termQuery 支持多种格式查询，如 boolean、int、double、string 等，这里使用的是 string 的查询）
                SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
                searchSourceBuilder.query(QueryBuilders.termQuery(esRequestParam.getQuery(), esRequestParam.getValue()));
                // 创建查询请求对象，将查询对象配置到其中
                SearchRequest searchRequest = new SearchRequest(esRequestParam.getIndex());
                searchRequest.source(searchSourceBuilder);
                // 执行查询，然后处理响应结果
                searchResponse = esRequestParam.getRestHighLevelClient().search(searchRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                log.error("", e);
            }
            return searchResponse;
        }
    },

    MATCH_ALL_QUERY("matchAllQuery") {
        @Override
        public SearchResponse initReturn(EsRequestParam esRequestParam) {
            SearchResponse searchResponse = null;
            try {
                // 构建查询条件
                MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
                // 创建查询源构造器
                SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
                searchSourceBuilder.query(matchAllQueryBuilder);
                // 设置分页
                searchSourceBuilder.from(esRequestParam.getPageStart());
                searchSourceBuilder.size(esRequestParam.getPageEnd());
                // 设置排序
                searchSourceBuilder.sort(esRequestParam.getQuery(), SortOrder.ASC);
                // 创建查询请求对象，将查询对象配置到其中
                SearchRequest searchRequest = new SearchRequest(esRequestParam.getIndex());
                searchRequest.source(searchSourceBuilder);
                // 执行查询，然后处理响应结果
                searchResponse = esRequestParam.getRestHighLevelClient().search(searchRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                log.error("", e);
            }
            return searchResponse;
        }
    },

    FUZZY_QUERY("fuzzyQuery") {
        @Override
        public SearchResponse initReturn(EsRequestParam esRequestParam) {
            SearchResponse searchResponse = null;
            try {
                // 构建查询条件
                SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
                searchSourceBuilder.query(QueryBuilders.fuzzyQuery(esRequestParam.getQuery(), esRequestParam.getValue()).fuzziness(Fuzziness.AUTO));
                // 创建查询请求对象，将查询对象配置到其中
                SearchRequest searchRequest = new SearchRequest(esRequestParam.getIndex());
                searchRequest.source(searchSourceBuilder);
                // 执行查询，然后处理响应结果
                searchResponse = esRequestParam.getRestHighLevelClient().search(searchRequest, RequestOptions.DEFAULT);

            } catch (IOException e) {
                log.error("", e);
            }
            return searchResponse;
        }
    },

    RANGE_QUERY("rangeQuery") {
        @Override
        public SearchResponse initReturn(EsRequestParam esRequestParam) {
            SearchResponse searchResponse = null;
            try {
                SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
                searchSourceBuilder.query(QueryBuilders.rangeQuery(esRequestParam.getQuery()).gte(esRequestParam.getIndex()));
                // 创建查询请求对象，将查询对象配置到其中
                SearchRequest searchRequest = new SearchRequest(esRequestParam.getIndex());
                searchRequest.source(searchSourceBuilder);
                // 执行查询，然后处理响应结果
                searchResponse = esRequestParam.getRestHighLevelClient().search(searchRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                log.error("", e);
            }
            return searchResponse;
        }
    },

    WILDCARD_QUERY("wildcardQuery") {
        @Override
        public SearchResponse initReturn(EsRequestParam esRequestParam) {
            SearchResponse searchResponse = null;
            try {
                // 构建查询条件
                SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
                searchSourceBuilder.query(QueryBuilders.wildcardQuery(esRequestParam.getQuery(), (String) esRequestParam.getValue()[0]));
                // 创建查询请求对象，将查询对象配置到其中
                SearchRequest searchRequest = new SearchRequest(esRequestParam.getIndex());
                searchRequest.source(searchSourceBuilder);
                // 执行查询，然后处理响应结果
                searchResponse = esRequestParam.getRestHighLevelClient().search(searchRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                log.error("", e);
            }
            return searchResponse;
        }
    },

    ERROR_TYPE("没有此匹配模式"),

    POLYMERIZATION("polymerization") {
        @Override
        public Return getReturn() {
            Return aReturn = new PolymerizationReturn();
            log.info("Use polymerization mode");
            return aReturn;
        }

        @Override
        public SearchResponse initReturn(EsRequestParam esRequestParam) {
            SearchRequest request = new SearchRequest(esRequestParam.getIndex());
            return super.initReturn(esRequestParam);
        }
    };

    private static final Map<String, Mode> ENUM_MAP;

    static {
        Map<String, Mode> map = new ConcurrentHashMap<String, Mode>();
        for (Mode instance : Mode.values()) {
            map.put(instance.getName().toLowerCase(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    private String name;

    Mode(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static Mode getModel(String model) {
        Mode reMode = StringUtils.isBlank(model) ? INDEX_ALL : ENUM_MAP.get(model.toLowerCase());
        return reMode;
    }

    @Override
    public Return getReturn() {
        Return aReturn = new SearchReturn();
        log.info("default:Search return mode Or " + ERROR_TYPE.name);
        return aReturn;
    }

    @Override
    public SearchResponse initReturn(EsRequestParam esRequestParam) {
        log.error("error:No type found" + ERROR_TYPE.name);
        return null;
    }
}
