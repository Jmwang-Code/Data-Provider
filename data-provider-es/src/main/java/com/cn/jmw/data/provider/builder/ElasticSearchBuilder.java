package com.cn.jmw.data.provider.builder;

import com.cn.jmw.data.provider.es.entity.EsRequestParam;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月09日 17:38
 * @Version 1.0
 */
 public class ElasticSearchBuilder {

    public ElasticSearchPluginManager prepareVegMeal(EsRequestParam esRequestParam) {
        ElasticSearchPluginManager meal = new ElasticSearchPluginManager(esRequestParam);
        meal.addItem(new VegBurger());
        meal.addItem(new Coke());
        return meal;
    }

    public ElasticSearchPluginManager prepareNonVegMeal(EsRequestParam esRequestParam) {
        ElasticSearchPluginManager meal = new ElasticSearchPluginManager(esRequestParam);
        meal.addItem(new ChickenBurger());
        meal.addItem(new Pepsi());
        return meal;
    }
}