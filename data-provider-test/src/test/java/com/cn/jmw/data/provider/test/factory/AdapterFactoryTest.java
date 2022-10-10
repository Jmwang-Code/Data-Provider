package com.cn.jmw.data.provider.test.factory;

import com.cn.jmw.data.provider.base.bean.JdbcDriverInfo;
import com.cn.jmw.data.provider.jdbc.factory.AdapterFactory;
import org.junit.Test;

import java.util.List;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年10月09日 17:32
 * @Version 1.0
 */
public class AdapterFactoryTest {

    @Test
    public void loadDriverInfoFromResource(){
        List<JdbcDriverInfo> jdbcDriverInfos = AdapterFactory.loadDriverInfoFromResource();
        System.out.println(jdbcDriverInfos);
    }
}
