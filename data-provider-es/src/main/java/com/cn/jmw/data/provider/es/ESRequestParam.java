package com.cn.jmw.data.provider.es;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;

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
public class ESRequestParam {

    private String name;

    private String host;

    private String port;

    private String scheme;

    private String query;

    private String password;

    private String username;

    @Builder.Default
    private Integer connectTimeout = 5000;

    @Builder.Default
    private Integer socketTimeout = 5000;

    @Builder.Default
    private Integer connectionRequestTimeout = 5000;

    //异步连接数配置
    @Builder.Default
    private Integer maxConnTotal = 100;

    @Builder.Default
    private Integer maxConnPerRoute = 100;

    private String index;

    private String id;

    private Model model;

    public String toCacheKey() {
        return new StringJoiner(":").add(host)
                .add(port.toString())
                .add(scheme.toString())
                .add(query).toString();
    }

    enum Model{
        INDEX_ALL("index_all"),
        INDEX_ID("index_id");
        private static final Map<String,Model> ENUM_MAP;

        static {
            Map<String,Model> map = new ConcurrentHashMap<String, Model>();
            for (Model instance : Model.values()) {
                map.put(instance.getName().toLowerCase(),instance);
            }
            ENUM_MAP = Collections.unmodifiableMap(map);
        }
        private String name;

        Model(String name){
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public static Model getModel(String model){
            Model reModel = StringUtils.isBlank(model)?INDEX_ALL:ENUM_MAP.get(model.toLowerCase());
            return reModel;
        }
    }

}
