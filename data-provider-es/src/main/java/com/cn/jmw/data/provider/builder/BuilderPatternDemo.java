package com.cn.jmw.data.provider.builder;

import com.cn.jmw.data.provider.es.entity.EsRequestParam;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月09日 17:39
 * @Version 1.0
 */
public class BuilderPatternDemo {

    /**
     *  objectObjectHashMap.put(" name ", " testOne ");
     *         objectObjectHashMap.put("address", "152.136.154.249:9200");
     *         objectObjectHashMap.put("scheme", "http");
     *         objectObjectHashMap.put("query", "");
     *         objectObjectHashMap.put("username", "");
     *         objectObjectHashMap.put("password", "");
     *         objectObjectHashMap.put("index", "kibana_sample_data_ecommerce");
     *         objectObjectHashMap.put("model", "indexId");//index-id
     *         objectObjectHashMap.put("id", "-43_74MBeofnJbrlVzm1");
     */
    public static void main(String[] args) {
        EsRequestParam esRequestParam = EsRequestParam.builder()
                .size(3)
                .pageStart(0)
                .address("152.136.154.249:9200")
                .schema("http")
                .index("kibana_sample_data_ecommerce")
                .id("43_74MBeofnJbrlVzm1")
                .build();

        ElasticSearchBuilder elasticSearchBuilder = new ElasticSearchBuilder(esRequestParam);

        ElasticSearchPluginManager vegMeal = elasticSearchBuilder.prepareVegMeal(esRequestParam);
        System.out.println("Veg Meal");
        vegMeal.showItems();
        System.out.println("Total Cost: " +vegMeal.function());

        ElasticSearchPluginManager nonVegMeal = elasticSearchBuilder.prepareNonVegMeal(esRequestParam);
        System.out.println("\n\nNon-Veg Meal");
        nonVegMeal.showItems();
        System.out.println("Total Cost: " +nonVegMeal.function());
    }
}