package com.cn.jmw.data.provider.base.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author jmw
 * @Description 数据提供程序工厂配置模板 对应 MODE-CONFIG.json文件
 * @date 2022年10月08日 23:41
 * @Version 1.0
 */
@Data
public class DataProviderFactoryConfigTemplate implements Serializable{

    private String type;

    private String name;

    private String displayName;

    private List<Attribute> attributes;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Attribute implements Serializable {

        private String name;

        private String displayName;

        private boolean required;

        private boolean encrypt;

        private Object defaultValue;

        private String key;

        private String type;

        private String description;

        private List<Object> options;

        private List<Attribute> children;

    }
}
