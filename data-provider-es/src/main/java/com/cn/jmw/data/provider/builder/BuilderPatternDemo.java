package com.cn.jmw.data.provider.builder;

import com.cn.jmw.data.provider.es.entity.EsRequestParam;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月09日 17:39
 * @Version 1.0
 */
public class BuilderPatternDemo {
    public static void main(String[] args) {
        EsRequestParam esRequestParam = new EsRequestParam();
        ElasticSearchBuilder elasticSearchBuilder = new ElasticSearchBuilder(esRequestParam);

        ElasticSearchPluginManager vegMeal = elasticSearchBuilder.prepareVegMeal(esRequestParam);
        System.out.println("Veg Meal");
        vegMeal.showItems();
        System.out.println("Total Cost: " +vegMeal.getCost());

        ElasticSearchPluginManager nonVegMeal = elasticSearchBuilder.prepareNonVegMeal(esRequestParam);
        System.out.println("\n\nNon-Veg Meal");
        nonVegMeal.showItems();
        System.out.println("Total Cost: " +nonVegMeal.getCost());
    }
}