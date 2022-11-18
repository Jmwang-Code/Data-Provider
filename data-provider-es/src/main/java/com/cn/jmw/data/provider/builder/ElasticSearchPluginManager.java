package com.cn.jmw.data.provider.builder;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月09日 17:38
 * @Version 1.0
 */
import com.cn.jmw.data.provider.ThreadLocalCache;
import com.cn.jmw.data.provider.builder.aop.Times;
import com.cn.jmw.data.provider.builder.operator.Action;
import com.cn.jmw.data.provider.builder.plugins.Close;
import com.cn.jmw.data.provider.builder.plugins.Plugins;
import com.cn.jmw.data.provider.es.entity.EsRequestParam;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class ElasticSearchPluginManager implements Close {

    private ThreadLocalCache threadLocalCache;

    public ElasticSearchPluginManager(ThreadLocalCache threadLocalCache){
        this.threadLocalCache = threadLocalCache;
    }
    private List<Plugins> items = new ArrayList<Plugins>();

    public void addItem(Plugins item){
        items.add(item);
    }

    public float function(){
        return 0f;
    }

    public void showItems(){
        SearchRequest searchRequest = null;
        for (Plugins item : items) {
            item.append();
        }
    }

    @Action(name = "clones", description = "关闭函数")
    @Override
    public void clones(){
        EsRequestParam esRequestParam = (EsRequestParam)threadLocalCache.get("esRequestParam");
        RestHighLevelClient restHighLevelClient = esRequestParam.getRestHighLevelClient();
        try {
            if (restHighLevelClient!=null)restHighLevelClient.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}