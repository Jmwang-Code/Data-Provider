package com.cn.jmw.data.provider.builder.plugins.plu.page;

import com.cn.jmw.data.provider.ThreadLocalCache;
import com.cn.jmw.data.provider.builder.Plugin;
import com.cn.jmw.data.provider.builder.plugins.plu.PagePlugin;
import com.cn.jmw.data.provider.es.entity.EsRequestParam;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * @author jmw
 * @Description scroll+size在ES查询数据的方式：
 * 第一步先将用户指定的关键进行分词。
 * 第二步将词汇去分词库中进行检索，得到多个文档的id。
 * 第三步将文档的id存放在一个ES的上下文中。
 * 第四步根据你指定的size的个数去ES中检索指定个数的数据，拿完数据的文档id，会从上下文中移除。
 * 第五步如果需要下一页数据，直接去ES的上下文中，找后续内容。
 * 第六步循环第四步和第五步
 * @date 2022年11月09日 17:37
 * @Version 1.0
 *
 */
public class ScrollPage extends PagePlugin {

    private int scrollSeconds;

    private int size;

    private EsRequestParam esRequestParam;

    private SearchSourceBuilder searchSourceBuilder;

    private SearchRequest searchRequest;

    public ScrollPage(ThreadLocalCache threadLocalCache){
        esRequestParam = (EsRequestParam)threadLocalCache.get(Plugin.ES_REQUEST_PARAM);
        searchSourceBuilder = (SearchSourceBuilder)threadLocalCache.get(Plugin.SEARCH_SOURCE_BUILDER);
        searchRequest = (SearchRequest)threadLocalCache.get(Plugin.SEARCH_REQUEST);
        this.scrollSeconds = esRequestParam.getScrollMinutes();
        this.size = esRequestParam.getSize();
    }

    @Override
    public void append(ThreadLocalCache threadLocalCache) {
        if (scrollSeconds>0){
            Scroll scroll = new Scroll(TimeValue.timeValueSeconds(scrollSeconds));
            searchRequest.scroll(scroll);
        }
        if (size>=0)searchSourceBuilder.size(size);
    }

}