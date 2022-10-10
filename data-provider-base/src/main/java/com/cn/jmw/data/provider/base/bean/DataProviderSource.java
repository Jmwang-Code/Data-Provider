package com.cn.jmw.data.provider.base.bean;

import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * @author jmw
 * @Description 接口参数 —— 数据提供源
 * @date 2022年10月09日 15:20
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataProviderSource {

    private String sourceId;

    private String type;

    private String name;

    private Map<String, Object> properties;

}