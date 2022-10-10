package com.cn.jmw.data.provider.base.factory;

import com.cn.jmw.data.provider.base.bean.DataProviderFactoryConfigTemplate;
import com.cn.jmw.data.provider.base.bean.DataProviderInfo;
import com.cn.jmw.data.provider.base.bean.DataProviderSource;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年10月08日 18:23
 * @Version 1.0
 */
public abstract class DataProviderAbstractFactory {

    //对象映射器
    private static ObjectMapper MAPPER = new ObjectMapper();

    public abstract Object test(DataProviderSource source) throws Exception;

    /**
     * 配置文件加载功能 - 获取JSON配置文件名
     */
    public abstract String getConfigJsonFileName();

    /**
     * 配置文件加载功能 - 获取配置模板
     */
    public DataProviderFactoryConfigTemplate getConfigTemplate() throws IOException {
        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(getConfigJsonFileName())) {
            return MAPPER.readValue(resourceAsStream, DataProviderFactoryConfigTemplate.class);
        }
    }

    /**
     * 配置文件加载功能 - 获取基本信息
     */
    public DataProviderInfo getBaseInfo() throws IOException {
        DataProviderFactoryConfigTemplate template = getConfigTemplate();
        DataProviderInfo dataProviderInfo = new DataProviderInfo();
        dataProviderInfo.setName(template.getName());
        dataProviderInfo.setType(template.getType());
        return dataProviderInfo;
    }

    /**
     * 配置文件加载功能 - 获取对应抽象工厂功能
     */
    public String getType() throws IOException {
        return getBaseInfo().getType();
    }



}
