package com.cn.jmw.data.provider.base.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author jmw
 * @Description 配置文件 —— 数据提供者基本信息
 * @date 2022年10月09日 0:03
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
public class DataProviderInfo implements Serializable {

    private String type;

    private String name;

    @Override
    public String toString() {
        return "DataProviderInfo{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}