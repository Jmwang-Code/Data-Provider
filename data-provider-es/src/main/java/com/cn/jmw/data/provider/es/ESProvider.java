package com.cn.jmw.data.provider.es;

import com.cn.jmw.data.provider.base.entity.DataSourceProviderEntity;
import com.cn.jmw.data.provider.base.entity.common.ValueType;
import com.cn.jmw.data.provider.base.entity.db.Column;
import com.cn.jmw.data.provider.base.entity.db.Dataframe;
import com.cn.jmw.data.provider.base.entity.db.Dataframes;
import com.cn.jmw.data.provider.base.factory.DataProviderAbstractDefaultFactory;
import com.cn.jmw.data.provider.base.factory.DataProviderAbstractFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
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
import java.util.stream.Collectors;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年10月17日 18:08
 * @Version 1.0
 */
public class ESProvider extends DataProviderAbstractDefaultFactory {

    private final String CONFIG = "ES-CONFIG.json";

    @Override
    public String getConfigJsonFileName() {
        return CONFIG;
    }

    @Override
    public Dataframes fullLoadOfDataSource(DataSourceProviderEntity config) throws Exception {
        //转换请求参数
        List<ESRequestParam> esRequestParams = convertRequestParams(config);
        if (esRequestParams.size()==0 || esRequestParams==null) {
            return Dataframes.of(config.getSourceId());
        }
        //获取MD5key
        String dataKey = DigestUtils.md5Hex(esRequestParams.stream()
                .map(ESRequestParam::toCacheKey)
                .collect(Collectors.joining(",")));

        Dataframes dataframes = Dataframes.of(dataKey);

        for (ESRequestParam esRequestParam : esRequestParams) {
            Dataframe dataframe = new EsInfoProcessor(esRequestParam).fetchAndParse();
            dataframe.setName(esRequestParam.getName());
            if (dataframe.getName()==null)dataframe.setName(esRequestParam.getIndex()+esRequestParam.getId());
            dataframes.add(dataframe);
        }
        return dataframes;
    }

    private List<ESRequestParam> convertRequestParams(DataSourceProviderEntity source) throws ClassNotFoundException {
        List<ESRequestParam> esRequestParams = new ArrayList<>();
        Map<String, Object> properties = source.getProperties();
        esRequestParams.add(ESRequestParam.builder()
                .port((String) properties.get("port"))
                .host((String) properties.get("host"))
                .query((String) properties.get("query"))
                .scheme((String) properties.get("scheme"))
                .username((String) properties.get("username"))
                .password((String) properties.get("password"))
                .index((String) properties.get("index"))
                .model(ESRequestParam.Model.getModel((String) properties.get("model")))
                .id((String) properties.get("id"))
                .build());
        return esRequestParams;
    }

    @Slf4j
    static class EsInfoProcessor {

        private ESRequestParam esRequestParam;

        public EsInfoProcessor(ESRequestParam esRequestParam) {
            this.esRequestParam = esRequestParam;
        }

        public Dataframe fetchAndParse() throws IOException {
            Dataframe dataframe = new Dataframe();
            RestHighLevelClient init = init();

            SearchRequest request = null;
            //1、查询索引中全部数据
            switch (esRequestParam.getModel()) {
                case INDEX_ID:
                    request = new SearchRequest(esRequestParam.getIndex());
                    request.source(new SearchSourceBuilder().query(QueryBuilders.termQuery("_id", esRequestParam.getId())));
                    break;
                case INDEX_ALL: {
//                    request = new GetRequest(esRequestParam.getIndex());
                    request = new SearchRequest();
                    request.indices(esRequestParam.getIndex());
                    request.source(new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()));
                    break;
                }
                default:
                    log.error("The elasticSearch index or id location failed");
            }

            SearchResponse response = init.search(request, RequestOptions.DEFAULT);

            SearchHits hits = response.getHits();
//            System.out.println(hits.getTotalHits());
//            System.out.println(response.getTook());

            dataframe.setColumns(new ArrayList<>());
            dataframe.setRows(new ArrayList<>());
            List<Column> columns = dataframe.getColumns();
            List<List<Object>> rows = dataframe.getRows();

            SearchHit at = hits.getAt(0);
            Map<String, Object> sourceAsMap1 = at.getSourceAsMap();
            Iterator<String> iterator1 = at.getSourceAsMap().keySet().iterator();
            while (iterator1.hasNext()) {
                //拿到key值
                String key = iterator1.next();
                Column column = new Column();
                column.setType(ValueType.STRING);
                column.setName(new String[]{key});
                columns.add(column);
            }

            for (SearchHit hit : hits) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                Iterator<String> iterator = hit.getSourceAsMap().keySet().iterator();
                List<Object> objects = new ArrayList<>();
                while (iterator.hasNext()) {
                    //拿到key值
                    String key = iterator.next();
                    objects.add(sourceAsMap.get(key));
                }
                rows.add(objects);
            }

            return dataframe;
        }

        private RestHighLevelClient init() {
            List<HttpHost> hostLists = new ArrayList<>();
            String[] hostList = (esRequestParam.getHost() + ":" + esRequestParam.getPort()).split(",");
            for (String addr : hostList) {
                String host = addr.split(":")[0];
                String port = addr.split(":")[1];
                hostLists.add(new HttpHost(host, Integer.parseInt(port), esRequestParam.getScheme()));
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

}
