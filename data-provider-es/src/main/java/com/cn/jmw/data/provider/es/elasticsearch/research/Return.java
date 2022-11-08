package com.cn.jmw.data.provider.es.elasticsearch.research;

import com.cn.jmw.data.provider.es.entity.EsRequestParam;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月07日 14:31
 * @Version 1.0
 */
public interface Return {

    /**
     * @Description init
     *
     *
     *
     * @Author jmw
     * @Date 16:15 2022/11/7
     */
    Return init(EsRequestParam esRequestParam);

    /**
     * @Description start
     *
     *
     *
     * @Author jmw
     * @Date 16:14 2022/11/7
     */
    <T> T start(T t);
}
