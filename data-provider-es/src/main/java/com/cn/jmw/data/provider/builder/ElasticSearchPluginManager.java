package com.cn.jmw.data.provider.builder;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月09日 17:38
 * @Version 1.0
 */
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

    public float getCost(){
        float cost = 0.0f;
        for (Plugins item : items) {
            cost += item.price();
        }
        return cost;
    }

    public void showItems(){
        for (Plugins item : items) {
            System.out.print("Item : "+item.name());
            System.out.print(", Packing : "+item.packing().pack());
            System.out.println(", Price : "+item.price());
        }
    }
}