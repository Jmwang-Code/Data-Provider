package com.cn.jmw.data.provider.builder.plugins.plu.sort;

import com.cn.jmw.data.provider.ThreadLocalCache;
import com.cn.jmw.data.provider.builder.Plugin;
import com.cn.jmw.data.provider.builder.en.Sort;
import com.cn.jmw.data.provider.builder.plugins.plu.SortPlugin;
import com.cn.jmw.data.provider.es.entity.EsRequestParam;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月09日 17:37
 * @Version 1.0
 */
public class Ascending extends SortPlugin {

    private EsRequestParam esRequestParam;

    private SearchSourceBuilder searchSourceBuilder;

    private SortOrder sort;

    private String sortName;

    public Ascending(ThreadLocalCache threadLocalCache) {
        esRequestParam = (EsRequestParam)threadLocalCache.get(Plugin.ES_REQUEST_PARAM);
        searchSourceBuilder = (SearchSourceBuilder)threadLocalCache.get(Plugin.SEARCH_SOURCE_BUILDER);
        this.sort = Sort.StringToSort(esRequestParam.getSort());
        this.sortName = esRequestParam.getSortName();
    }

    @Override
    public void append(ThreadLocalCache threadLocalCache) {
        if (sort!=null){
            if (StringUtils.isNotBlank(sortName))searchSourceBuilder.sort(sortName,sort);
            else searchSourceBuilder.sort(sortName);
        }
    }

}

//enum Sort {
//
//}