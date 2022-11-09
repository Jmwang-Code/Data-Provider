package com.cn.jmw.data.provider.es.elasticsearch;

import com.cn.jmw.data.provider.base.entity.common.ValueType;
import com.cn.jmw.data.provider.base.entity.db.Column;
import com.cn.jmw.data.provider.base.entity.db.Dataframe;
import com.cn.jmw.data.provider.es.elasticsearch.research.Return;
import com.cn.jmw.data.provider.es.entity.EsRequestParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月07日 14:31
 * @Version 1.0
 */
@Slf4j
public class EsInfoProcessor {

    private EsRequestParam esRequestParam;

    public EsInfoProcessor(EsRequestParam esRequestParam) {
        this.esRequestParam = esRequestParam;
    }

    public Dataframe fetchAndParse() throws IOException {
        Dataframe dataframe = new Dataframe();
        RestHighLevelClient restHighLevelClient = init();
        esRequestParam.setRestHighLevelClient(restHighLevelClient);
        Return aReturn = esRequestParam.getMode().getReturn();
        dataframe = aReturn.init(esRequestParam).start(dataframe);
        return dataframe;
    }

    private RestHighLevelClient init() {
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
        return new RestHighLevelClient(builder);
    }

}
