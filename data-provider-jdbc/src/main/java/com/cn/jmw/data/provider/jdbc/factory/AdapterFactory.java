package com.cn.jmw.data.provider.jdbc.factory;

import com.cn.jmw.data.provider.base.entity.JdbcDriverInfo;
import com.cn.jmw.data.provider.base.entity.JdbcProperties;
import com.cn.jmw.data.provider.base.utils.FileUtil;
import com.cn.jmw.data.provider.jdbc.adapter.JdbcDataProviderAdapter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

/**
 * @author jmw
 * @Description 数据源驱动 加载 管理
 * 扩展数据源 加载 管理
 * @date 2022年10月09日 16:06
 * @Version 1.0
 */
@Slf4j
public class AdapterFactory {

    //驱动池
    private static final Map<String, JdbcDriverInfo> jdbcDriverInfoMap = new ConcurrentSkipListMap<>();

    //公共类驱动
    private static final String JDBC_DRIVER_BUILD_IN = "/jdbc-driver.yml";

    //扩展类驱动
    private static final String JDBC_DRIVER_EXT = "/config/jdbc-driver-ext.yml";

    //
    public static final String DEFAULT_ADAPTER = "com.cn.jmw.data.provider.jdbc.adapter.JdbcDataProviderAdapter";


    /**
     * @Author jmw
     * @Description 做初始化 创建适配器
     * @Date 16:27 2022/10/9
     */
    public static JdbcDataProviderAdapter createDataAdapter(JdbcProperties source, boolean init) {
        //多种JDBC加载驱动信息
        List<JdbcDriverInfo> jdbcDriverInfos = loadDriverInfoFromResource();
        //过滤掉此次不需要的适配器
        List<JdbcDriverInfo> driverInfos = jdbcDriverInfos.stream().filter(item -> source.getDbType().equals(item.getDbType()))
                .collect(Collectors.toList());
        if (driverInfos.size() == 0) {
            log.error("message.provider.jdbc.dbtype", source.getDbType());
        }
        if (driverInfos.size() > 1) {
            log.error("Duplicated dbType " + source.getDbType());
        }

        JdbcDriverInfo driverInfo = driverInfos.get(0);

        if (StringUtils.isNotBlank(source.getDriverClass())) {
            driverInfo.setDriverClass(source.getDriverClass());
        }

        /**
         * 获取适配器
         */
        JdbcDataProviderAdapter adapter = null;
        try {
            if (StringUtils.isNotBlank(driverInfo.getAdapterClass())) {
                try {
                    //通过配置全路径名的方式获取，反射创建
                    Class<?> aClass = Class.forName(driverInfo.getAdapterClass());
                    adapter = (JdbcDataProviderAdapter) aClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    log.error("Jdbc adapter class (" + driverInfo.getAdapterClass() + ") load error.use default adapter");
                }
            }
            if (adapter == null) {
                adapter = (JdbcDataProviderAdapter) Class.forName(DEFAULT_ADAPTER).getDeclaredConstructor().newInstance();
            }
        } catch (Exception e) {
            log.error("Jdbc adapter class load error ", e);
        }
        if (adapter == null) {
            log.info( "message.provider.jdbc.create.error", source.getDbType());
        }
        if (init) {
            adapter.init(source, driverInfo);
        }
        return adapter;
    }

    /**
     * @Author jmw
     * @Description 从资源加载驱动程序信息
     * @Date 16:50 2022/10/9
     */
    public static List<JdbcDriverInfo> loadDriverInfoFromResource() {
        ObjectMapper objectMapper = new ObjectMapper();
        //设置属性命名策略 用作全局命名
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
        //反序列化命名策略,未知属性失败
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //构建数据库类型
        Map<String, Map<String, String>> buildIn = loadYml(JDBC_DRIVER_BUILD_IN);
        //Owner用户数据库类型 TODO 暂时不支持外接扩展的类型，等以后开发专门对外保留扩展接口
//        Map<String, Map<String, String>> extDrivers = loadYml(new File(FileUtil.concatPath(System.getProperty("user.dir"), JDBC_DRIVER_EXT)));
        Map<String, Map<String, String>> extDrivers = loadYml(JDBC_DRIVER_EXT);

        //将扩展类的融合到 数据库类型中
        if (!CollectionUtils.isEmpty(extDrivers)) extDrivers.forEach((k, v) -> {
            if (buildIn.get(k) == null) {
                buildIn.put(k, v);
            } else {
                buildIn.get(k).putAll(extDrivers.get(k));
            }
        });

        //做处理
        return buildIn.entrySet().stream().map(entry -> {
            try {
                //转换值
                JdbcDriverInfo jdbcDriverInfo = objectMapper.convertValue(entry.getValue(), JdbcDriverInfo.class);
                if (StringUtils.isBlank(jdbcDriverInfo.getAdapterClass())) {
                    jdbcDriverInfo.setAdapterClass(DEFAULT_ADAPTER);
                }
                // default to quote all identifiers ,  for support special column names and most databases
                if (jdbcDriverInfo.getQuoteIdentifiers() == null) {
                    jdbcDriverInfo.setQuoteIdentifiers(true);
                }
                jdbcDriverInfo.setDbType(jdbcDriverInfo.getDbType().toUpperCase());
                return jdbcDriverInfo;
            } catch (Exception e) {
                log.error("DbType " + entry.getKey() + " driver read Exception", e);
            }
            return null;
        }).filter(Objects::nonNull).sorted(Comparator.comparing(JdbcDriverInfo::getDbType)).collect(Collectors.toList());
    }

    /**
     * @Author jmw
     * @Description 通过resources文件下获取驱动信息
     * @Date 17:20 2022/10/9
     */
    private static Map<String, Map<String, String>> loadYml(String file) {
        try (InputStream inputStream = AdapterFactory.class.getResourceAsStream(file)) {
            Yaml yaml = new Yaml();
            return yaml.loadAs(inputStream, HashMap.class);
        } catch (Exception e) {
            log.error(String.valueOf(e));
        }
        return null;
    }

    /**
     * @Author jmw
     * @Description 通过File类型加载成驱动文件
     * @Date 17:20 2022/10/9
     */
    public static Map<String, Map<String, String>> loadYml(File file) {
        try (InputStream inputStream = new FileInputStream(file)) {
            Yaml yaml = new Yaml();
            return yaml.loadAs(inputStream, HashMap.class);
        } catch (Exception e) {
            log.error(String.valueOf(e));
        }
        return null;
    }

    /**
     * @Author jmw
     * @Description 从jdbc驱动程序信息 缓冲池中获取配置文件
     * @Date 11:28 2022/10/13
     */
    public static JdbcDriverInfo getJdbcDriverInfo(String dbType) {
        if (jdbcDriverInfoMap.isEmpty()) {
            for (JdbcDriverInfo jdbcDriverInfo : loadDriverInfoFromResource()) {
                jdbcDriverInfoMap.put(jdbcDriverInfo.getDbType(), jdbcDriverInfo);
            }
        }
        return jdbcDriverInfoMap.get(dbType);
    }

}
