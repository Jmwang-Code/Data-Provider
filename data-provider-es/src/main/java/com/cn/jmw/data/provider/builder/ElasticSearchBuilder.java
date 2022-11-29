package com.cn.jmw.data.provider.builder;

import com.cn.jmw.data.provider.ThreadLocalCache;
import com.cn.jmw.data.provider.base.entity.db.Dataframe;
import com.cn.jmw.data.provider.builder.en.Page;
import com.cn.jmw.data.provider.builder.en.Query;
import com.cn.jmw.data.provider.builder.en.Sort;
import com.cn.jmw.data.provider.builder.plugins.plu.ClosePlugin;
import com.cn.jmw.data.provider.builder.plugins.plu.SortPlugin;
import com.cn.jmw.data.provider.builder.plugins.plu.page.FromSizePage;
import com.cn.jmw.data.provider.builder.plugins.plu.page.ScrollPage;
import com.cn.jmw.data.provider.builder.plugins.plu.query.AllQuery;
import com.cn.jmw.data.provider.builder.plugins.plu.sort.Ascending;
import com.cn.jmw.data.provider.es.entity.EsRequestParam;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.ArrayList;
import java.util.List;


@Slf4j
//@Data
/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月09日 17:38
 * @Version 1.0
 */
public class ElasticSearchBuilder {

    private ThreadLocalCache threadLocalCache;
    private ElasticSearchPluginManager meal;

    public ElasticSearchBuilder(ThreadLocalCache threadLocalCache) {
        this.threadLocalCache = threadLocalCache;
        List<HttpHost> hostLists = new ArrayList<>();
        EsRequestParam esRequestParam = (EsRequestParam) threadLocalCache.get(Plugin.ES_REQUEST_PARAM);
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

    /**
     * 适配1分页方式
     */
    public ElasticSearchBuilder setPage(Page page){
        ElasticSearchPluginManager meal = new ElasticSearchPluginManager(threadLocalCache);
        this.meal = meal;
        switch (page){
            case FROM_SIZE -> {meal.addItem(new FromSizePage(threadLocalCache));break;}
            case SCROLL_PAGE -> {meal.addItem(new ScrollPage(threadLocalCache));break;}
            default -> log.error("Paging mode not recognized");
        }
        return this;
    }

    public ElasticSearchBuilder setSort(Sort sort){
        meal.addItem(new Ascending(threadLocalCache));
        return this;
    }

    public ElasticSearchBuilder setQuery(Query query){
        switch (query){
            case INDEX_ALL -> {meal.addItem(new AllQuery(threadLocalCache));break;}
            default -> log.error("Querying mode not recognized");
        }
        return this;
    }

    public SearchResponse build(){
        meal.addItem(new ClosePlugin(threadLocalCache));
        meal.showItems(threadLocalCache);

        return (SearchResponse)threadLocalCache.get(Plugin.SEARCH_RESPONSE);
    }



//    public ElasticSearchPluginManager prepareVegMeal() {
//        ElasticSearchPluginManager meal = new ElasticSearchPluginManager(threadLocalCache);
//        //TODO 中间需要加一层 去通过ES参数 判断自适应Plugins
////        Plugins plugins = new FromSizePage(esRequestParam);
//        meal.addItem(new FromSizePage(threadLocalCache));
//        meal.addItem(new Ascending(threadLocalCache));
//        meal.addItem(new AllQuery(threadLocalCache));
//        meal.addItem(new ClosePlugin(threadLocalCache));
//        return meal;
//    }

}