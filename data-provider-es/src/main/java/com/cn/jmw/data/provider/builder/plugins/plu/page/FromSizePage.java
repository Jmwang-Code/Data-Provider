package com.cn.jmw.data.provider.builder.plugins.plu.page;

import com.cn.jmw.data.provider.ThreadLocalCache;
import com.cn.jmw.data.provider.builder.plugins.PagePlugin;
import com.cn.jmw.data.provider.es.entity.EsRequestParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月09日 17:36
 * @Version 1.0
 */
//@SuperBuilder
public class FromSizePage extends PagePlugin {

    private int from;
    private int size;

    private EsRequestParam esRequestParam;

    private SearchSourceBuilder searchSourceBuilder;
    public FromSizePage(ThreadLocalCache threadLocalCache){
        esRequestParam = (EsRequestParam)threadLocalCache.get("esRequestParam");
        searchSourceBuilder = (SearchSourceBuilder)threadLocalCache.get("searchSourceBuilder");
        this.from = esRequestParam.getPageStart();
        this.size = esRequestParam.getSize();
    }

    @Override
    public void append() {
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
    }

}