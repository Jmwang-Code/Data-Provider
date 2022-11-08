package com.cn.jmw.data.provider.es.elasticsearch;

import com.cn.jmw.data.provider.es.elasticsearch.research.Return;
import com.cn.jmw.data.provider.es.entity.EsRequestParam;
import org.elasticsearch.action.search.SearchRequest;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月07日 17:08
 * @Version 1.0
 */
public interface ModeInter {

    /**
     * @Description 获取Return对象
     * 
     * 
     * 
     * @Author jmw
     * @Date 17:10 2022/11/7
     */
    Return getReturn();

    /**
     * @Description 初始化具体类型
     *
     *
     *
     * @Author jmw
     * @Date 17:51 2022/11/7
     */
    SearchRequest setReturn(EsRequestParam esRequestParam);
}
