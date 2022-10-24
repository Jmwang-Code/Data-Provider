package com.cn.jmw.data.provider.test.local;

import com.cn.jmw.data.provider.localdb.annotation.EnableLocalDB;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年10月23日 18:32
 * @Version 1.0
 */
@EnableLocalDB
@SpringBootTest
public class Local {

    @Test
    public void test(){
    }
}
