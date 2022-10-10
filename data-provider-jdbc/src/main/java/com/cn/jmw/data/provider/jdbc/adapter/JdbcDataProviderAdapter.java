package com.cn.jmw.data.provider.jdbc.adapter;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年10月08日 18:25
 * @Version 1.0
 */
public class JdbcDataProviderAdapter implements Closeable {

    /**
     * @Author jmw
     * @Description 销毁
     * @Date 18:29 2022/10/8
     */
    @Override
    public void close() throws IOException {

    }

    public boolean test(){
        return true;
    }
}
