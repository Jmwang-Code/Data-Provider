package com.cn.jmw.data.provider.base.factory;

import com.cn.jmw.data.provider.base.entity.DataProviderFactoryConfigTemplate;
import com.cn.jmw.data.provider.base.entity.DataProviderInfo;
import com.cn.jmw.data.provider.base.entity.DataSourceProviderEntity;
import com.cn.jmw.data.provider.base.entity.db.Dataframe;
import com.cn.jmw.data.provider.base.entity.db.ExecutionParam;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年10月08日 18:23
 * @Version 1.0
 */
public abstract class DataProviderAbstractFactory {

    //对象映射器
    private static final ObjectMapper OBJECTMAPPER = new ObjectMapper();

    /**
     * Generally,only testing
     */
    public abstract Object test(DataSourceProviderEntity source) throws Exception;

    /**
     * Generally,only execution
     */
    public abstract Dataframe execute(DataSourceProviderEntity source, ExecutionParam executionParam) throws Exception;

    /**
     * 配置文件加载功能 - 获取JSON配置文件名
     */
    public abstract String getConfigJsonFileName();

    /**
     * 配置文件加载功能 - 获取配置模板
     */
    public DataProviderFactoryConfigTemplate getConfigTemplate() throws IOException {
        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(getConfigJsonFileName())) {
            return OBJECTMAPPER.readValue(resourceAsStream, DataProviderFactoryConfigTemplate.class);
        }
    }

    /**
     * 配置文件加载功能 - 获取基本信息
     */
    public DataProviderInfo getBaseInfo() throws IOException {
        DataProviderFactoryConfigTemplate template = getConfigTemplate();
        return DataProviderInfo
                .builder()
                .type(template.getType())
                .name(template.getName())
                .build();
    }

    /**
     * 配置文件加载功能 - 获取对应抽象工厂功能
     */
    public String getType() throws IOException {
        return getBaseInfo().getType();
    }



}
