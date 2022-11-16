package com.cn.jmw.data.provider.builder.plugins;

import com.cn.jmw.data.provider.builder.packing.Packing;
import com.cn.jmw.data.provider.es.entity.EsRequestParam;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月09日 17:34
 * @Version 1.0
 */
public interface Plugins {

    public Packing packing();
    public float build();
}