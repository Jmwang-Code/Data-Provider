package com.cn.jmw.data.provider.es.elasticsearch;

import com.cn.jmw.data.provider.es.elasticsearch.research.PolymerizationReturn;
import com.cn.jmw.data.provider.es.elasticsearch.research.Return;
import com.cn.jmw.data.provider.es.elasticsearch.research.SearchReturn;
import com.cn.jmw.data.provider.es.entity.EsRequestParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

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
    INDEX_ALL("indexAll"){
        @Override
        public SearchRequest setReturn(EsRequestParam esRequestParam) {
            SearchRequest request = new SearchRequest(esRequestParam.getIndex());
            request.source(new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()));
            return request;
        }
    },

    INDEX_ID("indexId"){
        @Override
        public SearchRequest setReturn(EsRequestParam esRequestParam) {
            SearchRequest request = new SearchRequest(esRequestParam.getIndex());
            request.source(new SearchSourceBuilder().query(QueryBuilders.termQuery("_id", esRequestParam.getId())));
            return request;
        }
    },

    POLYMERIZATION("polymerization"){
        @Override
        public Return getReturn() {
            Return aReturn = new PolymerizationReturn();
            log.info("Use polymerization mode");
            return aReturn;
        }
    },

    //TODO
    TERM_QUERY("termQuery"),

    MATCH_ALL_QUERY("matchAllQuery"),

    FUZZY_QUERY("fuzzyQuery"),

    RANGE_QUERY("rangeQuery"),

    WILDCARD_QUERY("wildcardQuery"),

    BOOL_QUERY("boolQuery"),

    ERROR_TYPE("没有此匹配模式");

    private static final Map<String, Mode> ENUM_MAP;

    static {
        Map<String, Mode> map = new ConcurrentHashMap<String, Mode>();
        for (Mode instance : Mode.values()) {
            map.put(instance.getName().toLowerCase(),instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }
    private String name;

    Mode(String name){
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static Mode getModel(String model){
        Mode reMode = StringUtils.isBlank(model)?INDEX_ALL:ENUM_MAP.get(model.toLowerCase());
        return reMode;
    }

    @Override
    public Return getReturn() {
        Return aReturn = new SearchReturn();
        log.error("error:No type found"+ERROR_TYPE.name);
        return aReturn;
    }

    @Override
    public SearchRequest setReturn(EsRequestParam esRequestParam) {
        log.error("error:No type found"+ERROR_TYPE.name);
        return null;
    }
}
