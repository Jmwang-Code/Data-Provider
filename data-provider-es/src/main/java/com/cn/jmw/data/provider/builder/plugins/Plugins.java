package com.cn.jmw.data.provider.builder.plugins;

import com.cn.jmw.data.provider.ThreadLocalCache;
import com.cn.jmw.data.provider.builder.packing.Packing;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月09日 17:34
 * @Version 1.0
 */
public interface Plugins {

    public void append(ThreadLocalCache threadLocalCache);

}