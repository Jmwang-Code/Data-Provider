package com.cn.jmw.data.provider.builder.plugins.plu.page;

import com.cn.jmw.data.provider.builder.Plugin;
import com.cn.jmw.data.provider.ThreadLocalCache;
import com.cn.jmw.data.provider.builder.plugins.plu.PagePlugin;
import com.cn.jmw.data.provider.es.entity.EsRequestParam;
import org.elasticsearch.search.builder.SearchSourceBuilder;


/**
 * @author jmw
 * @Description from+size在ES查询数据的方式：
 * 第一步先将用户指定的关键进行分词。
 * 第二步将词汇去分词库中进行检索，得到多个文档的id。
 * 第三步去各个分片中去拉取指定的全部数据。耗时较长。
 * 第四步将数据根据score进行排序。耗时较长。
 * 第五步根据from的值，将查询到的数据舍弃一部分。
 * 第六步返回结果。
 * @date 2022年11月09日 17:36
 * @Version 1.0
 */
//@SuperBuilder
public class FromSizePage extends PagePlugin {

    private int from = -1;
    private int size = -1;

    private EsRequestParam esRequestParam;

    private SearchSourceBuilder searchSourceBuilder;

    public FromSizePage(ThreadLocalCache threadLocalCache){
        esRequestParam = (EsRequestParam)threadLocalCache.get(Plugin.ES_REQUEST_PARAM);
        searchSourceBuilder = (SearchSourceBuilder)threadLocalCache.get(Plugin.SEARCH_SOURCE_BUILDER);
        this.from = esRequestParam.getPageStart();
        this.size = esRequestParam.getSize();
    }

    @Override
    public void append(ThreadLocalCache threadLocalCache) {
        if (from>=0)searchSourceBuilder.from(from);
        if (size>=0)searchSourceBuilder.size(size);
    }

}