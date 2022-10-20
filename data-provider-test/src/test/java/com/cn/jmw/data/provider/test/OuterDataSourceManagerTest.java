package com.cn.jmw.data.provider.test;

import com.cn.jmw.data.provider.base.OuterDataSourceManager;
import com.cn.jmw.data.provider.base.entity.DataSourceProviderEntity;
import com.cn.jmw.data.provider.base.entity.common.DataSourceTypeEnum;
import com.cn.jmw.data.provider.base.entity.db.ExecutionParam;
import com.cn.jmw.data.provider.base.factory.DataProviderAbstractFactory;
import com.cn.jmw.data.provider.base.response.ResponseBody;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
/**
 * @author jmw
 * @Description TODO
 * @date 2022年10月09日 14:50
 * @Version 1.0
 */
public class OuterDataSourceManagerTest {
    static OuterDataSourceManager outerDataSourceManager = new OuterDataSourceManager();

    static HashMap map = new HashMap();
    static {
        map.put("dbType","MYSQL");
        map.put("driverClass","com.mysql.cj.jdbc.Driver");
        map.put("url","jdbc:mysql://152.136.154.249:3306/demo?&allowMultiQueries=true&useUnicode=true&characterEncoding=utf8&serverTimezone=UTC");
        map.put("user","demo");
        map.put("password","kKJ8XynXLzjfYDA7");
    }

    static DataSourceProviderEntity source = new DataSourceProviderEntity().builder()
            .type("JDBC")
            .name("DEMO")
            .sourceId("1")
            .properties(map)
            .build();

    @Test
    public void loadDataProviderFactories(){
        outerDataSourceManager.loadDataProviders();
        DataProviderAbstractFactory jdbc = outerDataSourceManager.cachedDataProviderAbstractFactories.get(DataSourceTypeEnum.JDBC.name());
        System.out.println(jdbc.getConfigJsonFileName());
    }

    @Test
    public void getDataProviderFactory(){
        DataProviderAbstractFactory jdbc = outerDataSourceManager.getDataProvider(DataSourceTypeEnum.JDBC.name());
        System.out.println(jdbc.getConfigJsonFileName());
    }

    @Test
    public void testConnection() throws Exception {
        ResponseBody responseBody = outerDataSourceManager.testConnection(source);

        ResponseBody responseBody2 = outerDataSourceManager.testConnection(source);
        System.out.println(responseBody +""+ responseBody2);
    }

    @Test
    public void execute() throws SQLException, IOException {
        ResponseBody execute = outerDataSourceManager.execute(source, ExecutionParam.builder().sql("SELECT * FROM role").build());
        System.out.println(execute.getStatus());
        Files.write(Paths.get("C:\\Users\\jmw\\Desktop\\1.txt"),execute.getStatus().toString().getBytes("UTF-8"));
    }

    //测试索引创建
    @Test
    public void contextLoads() throws IOException {
        List<HttpHost> hostLists = new ArrayList<>();
        String[] hostList = "192.168.103.47:9200".split(",");
        for (String addr : hostList) {
            String host = addr.split(":")[0];
            String port = addr.split(":")[1];
            hostLists.add(new HttpHost(host, Integer.parseInt(port), "http"));
        }

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic", "eoi@%TGB"));

        // 转换成 HttpHost 数组
        HttpHost[] httpHost = hostLists.toArray(new HttpHost[]{});
        // 构建连接对象
        RestClientBuilder builder = RestClient.builder(httpHost);
        // 异步连接延时配置
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(5000);
            requestConfigBuilder.setSocketTimeout(5000);
            requestConfigBuilder.setConnectionRequestTimeout(5000);
            return requestConfigBuilder;
        });
        // 异步连接数配置
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(100);
            httpClientBuilder.setMaxConnPerRoute(100);
            return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        });
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(builder);
//        //1.创建索引请求
//        CreateIndexRequest request = new CreateIndexRequest("asdadd_index");
//
//        //2.执行创建请求 IndecesClient 请求后获得响应
//        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
//
//        System.out.println(createIndexResponse);
        GetIndexRequest getIndexRequest = new GetIndexRequest("wjm_index");

        //2.client 索引操作 获取索引
        GetIndexResponse getIndexResponse = restHighLevelClient.indices().get(getIndexRequest, RequestOptions.DEFAULT);

        System.out.println(getIndexResponse);
    }
}
