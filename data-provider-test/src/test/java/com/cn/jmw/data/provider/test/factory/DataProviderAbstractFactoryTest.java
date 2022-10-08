package com.cn.jmw.data.provider.test.factory;

import com.cn.jmw.data.provider.base.factory.DataProviderAbstractFactory;
import com.cn.jmw.data.provider.jdbc.JdbcProvider;
import org.junit.Test;

import java.io.IOException;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年10月09日 0:14
 * @Version 1.0
 */
public class DataProviderAbstractFactoryTest {

    @Test
    public void getType() throws IOException {
        String type = new DataProviderAbstractFactory() {
            @Override
            public String getConfigJsonFile() {
                return "JDBC-CONFIG.json";
            }
        }.getType();
        System.out.println(type);
    }
}
