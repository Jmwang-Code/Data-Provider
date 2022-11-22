package com.cn.jmw.data.provider.builder;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月09日 17:38
 * @Version 1.0
 */
import com.cn.jmw.data.provider.ThreadLocalCache;
import com.cn.jmw.data.provider.builder.plugins.Plugins;
import com.cn.jmw.data.provider.builder.plugins.plu.Analysis;
import org.elasticsearch.action.search.SearchRequest;
import java.util.ArrayList;
import java.util.List;

public class ElasticSearchPluginManager {

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

    public void showItems(ThreadLocalCache threadLocalCache){
        SearchRequest searchRequest = null;
        for (Plugins item : items) {
            item.append(threadLocalCache);
        }
    }


}