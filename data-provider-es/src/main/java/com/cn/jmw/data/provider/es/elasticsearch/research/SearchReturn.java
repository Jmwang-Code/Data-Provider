package com.cn.jmw.data.provider.es.elasticsearch.research;

import com.cn.jmw.data.provider.es.entity.EsRequestParam;
import org.elasticsearch.action.search.SearchRequest;

/**
 * @author jmw
 * @Description TODO 精确查询
 * @date 2022年11月07日 14:32
 * @Version 1.0
 */
public class SearchReturn implements Return {

    private SearchRequest request = null;

    @Override
    public Return init(EsRequestParam esRequestParam) {
        request = esRequestParam.getMode().setReturn(esRequestParam);
        return this;
    }

    @Override
    public Object start(Object o) {
        //TODO
        return null;
    }
}
