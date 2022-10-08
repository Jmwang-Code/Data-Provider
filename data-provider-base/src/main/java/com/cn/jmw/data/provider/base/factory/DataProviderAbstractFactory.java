package com.cn.jmw.data.provider.base.factory;

import com.cn.jmw.data.provider.base.bean.DataProviderFactoryConfigTemplate;
import com.cn.jmw.data.provider.base.bean.DataProviderInfo;
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

    static ObjectMapper MAPPER = new ObjectMapper();

    public abstract String getConfigJsonFile();

    public DataProviderFactoryConfigTemplate getConfigTemplate() throws IOException {
        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(getConfigJsonFile())) {
            return MAPPER.readValue(resourceAsStream, DataProviderFactoryConfigTemplate.class);
        }
    }

    public String getType() throws IOException {
        return getBaseInfo().getType();
    }

    public DataProviderInfo getBaseInfo() throws IOException {
        DataProviderFactoryConfigTemplate template = getConfigTemplate();
        DataProviderInfo dataProviderInfo = new DataProviderInfo();
        dataProviderInfo.setName(template.getName());
        dataProviderInfo.setType(template.getType());
        return dataProviderInfo;
    }

}
