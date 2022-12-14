package com.cn.jmw.data.provider.es.entity;

import com.cn.jmw.data.provider.es.elasticsearch.Mode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.Scroll;

import java.util.StringJoiner;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年10月18日 17:02
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EsRequestParam {

    private String name;
    /**
     * 集群地址，如果有多个用“,”隔开
     * xxx.xxx.xxx.xxx:9200
     */
    private String address;

    /** 协议 */
    private String schema;

    // key1.key1.1
    private String query;

    //精确查询boolean、int、double、string
    private String[] value;

    public void setValue(String... value) {
        this.value = value;
    }

    //起始页
    @Builder.Default
    private Integer pageStart = -1;

    //结束页
    @Builder.Default
    private Integer pageEnd = -1;

    //长度
    @Builder.Default
    private Integer size = -1;

    private String password;

    private String username;

    /** 连接超时时间 */
    @Builder.Default
    private Integer connectTimeout = 5000;

    /** Socket 连接超时时间 */
    @Builder.Default
    private Integer socketTimeout = 10000;

    /** 获取连接的超时时间 */
    @Builder.Default
    private Integer connectionRequestTimeout = 5000;

    /** 最大连接数 */
    //异步连接数配置
    @Builder.Default
    private Integer maxConnTotal = 100;

    /** 最大路由连接数 */
    @Builder.Default
    private Integer maxConnPerRoute = 100;

    private String index;

    private String id;

    private Mode mode;

    private RestHighLevelClient restHighLevelClient;

    private int scroll;

    private String sort;

    private String sortName;

    /**
     * Minutes
     */
    private int scrollMinutes;

    /**
     * RangeQuery
     */
    private int gte;
    private int gt;
    private int lte;
    private int lt;

    public String toCacheKey() {
        return new StringJoiner(":")
                .add(address.toString())
                .add(schema.toString())
                .add(query).toString();
    }

}
