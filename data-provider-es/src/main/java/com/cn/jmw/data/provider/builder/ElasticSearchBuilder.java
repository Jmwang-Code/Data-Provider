package com.cn.jmw.data.provider.builder;

import com.cn.jmw.data.provider.builder.plugins.plu.ScrollPage;
import com.cn.jmw.data.provider.builder.plugins.plu.Ascending;
import com.cn.jmw.data.provider.builder.plugins.plu.Descending;
import com.cn.jmw.data.provider.builder.plugins.plu.FromSizePage;
import com.cn.jmw.data.provider.es.entity.EsRequestParam;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月09日 17:38
 * @Version 1.0
 */
public class ElasticSearchBuilder {

    private EsRequestParam esRequestParam;

    public ElasticSearchBuilder(EsRequestParam esRequestParam) {
        List<HttpHost> hostLists = new ArrayList<>();
        String[] hostList = esRequestParam.getAddress().split(",");
        for (String addr : hostList) {
            String host = addr.split(":")[0];
            String port = addr.split(":")[1];
            hostLists.add(new HttpHost(host, Integer.parseInt(port), esRequestParam.getSchema()));
        }
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(esRequestParam.getUsername(), esRequestParam.getPassword()));

        // 转换成 HttpHost 数组
        HttpHost[] httpHost = hostLists.toArray(new HttpHost[]{});
        // 构建连接对象
        RestClientBuilder builder = RestClient.builder(httpHost);
        // 异步连接延时配置
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(esRequestParam.getConnectTimeout());
            requestConfigBuilder.setSocketTimeout(esRequestParam.getSocketTimeout());
            requestConfigBuilder.setConnectionRequestTimeout(esRequestParam.getConnectionRequestTimeout());
            return requestConfigBuilder;
        });
        // 异步连接数配置
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(esRequestParam.getMaxConnTotal());
            httpClientBuilder.setMaxConnPerRoute(esRequestParam.getMaxConnPerRoute());
            return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        });
        esRequestParam.setRestHighLevelClient(new RestHighLevelClient(builder));
    }

    public ElasticSearchPluginManager prepareVegMeal(EsRequestParam esRequestParam) {
        ElasticSearchPluginManager meal = new ElasticSearchPluginManager(esRequestParam);
        meal.addItem(FromSizePage.builder().build());
//        meal.addItem(new Ascending());
        return meal;
    }

    public ElasticSearchPluginManager prepareNonVegMeal(EsRequestParam esRequestParam) {
        ElasticSearchPluginManager meal = new ElasticSearchPluginManager(esRequestParam);
//        meal.addItem(new ScrollPage());
//        meal.addItem(new Descending());
        return meal;
    }
}