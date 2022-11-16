package com.cn.jmw.data.provider.builder;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月09日 17:38
 * @Version 1.0
 */
import com.cn.jmw.data.provider.builder.plugins.Plugins;
import com.cn.jmw.data.provider.es.entity.EsRequestParam;

import java.util.ArrayList;
import java.util.List;

public class ElasticSearchPluginManager {

    public ElasticSearchPluginManager(EsRequestParam esRequestParam){
        this.esRequestParam = esRequestParam;
    }

    private EsRequestParam esRequestParam;
    private List<Plugins> items = new ArrayList<Plugins>();

    public void addItem(Plugins item){
        items.add(item);
    }

    public float function(){
        return 0f;
    }

    public void showItems(){
        for (Plugins item : items) {
            System.out.print("Item : "+item.build());
//            System.out.print(", Packing : "+item.packing().selecting());
        }
    }
}