package com.cn.jmw.data.provider.builder;

import com.cn.jmw.data.provider.builder.packing.Packing;
import com.cn.jmw.data.provider.builder.plugins.Close;
import com.cn.jmw.data.provider.builder.plugins.ClosePlugin;
import com.cn.jmw.data.provider.builder.plugins.plu.page.FromSizePage;
import com.cn.jmw.data.provider.es.entity.EsRequestParam;
import lombok.Data;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Data
/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月09日 17:38
 * @Version 1.0
 */
public class ElasticSearchBuilder {

    private EsRequestParam esRequestParam;

    public ElasticSearchBuilder(EsRequestParam esRequestParam) {
        this.esRequestParam = esRequestParam;
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

    public ElasticSearchPluginManager prepareVegMeal() {
        ElasticSearchPluginManager meal = new ElasticSearchPluginManager(esRequestParam);
        meal.addItem(FromSizePage.builder().esRequestParam(esRequestParam).build());
        meal.addItem(new ClosePlugin() {
            @Override
            public void append() {
                meal.clones();
            }
        });
        return meal;
    }

    public ElasticSearchPluginManager prepareNonVegMeal() {
        ElasticSearchPluginManager meal = new ElasticSearchPluginManager(esRequestParam);
//        meal.addItem(new ScrollPage());
//        meal.addItem(new Descending());
        return meal;
    }


}