package com.cn.jmw.data.provider.base.entity.common;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年10月09日 15:03
 * @Version 1.0
 */
public enum DataSourceTypeEnum {

    JDBC("JDBC"),
    FILE("FILE"),
    HTTP("HTTP"),
    KAFKA("KAFKA");

    private String type;

    DataSourceTypeEnum(String type) {
        this.type = type;
    }

    DataSourceTypeEnum() {

    }
}
