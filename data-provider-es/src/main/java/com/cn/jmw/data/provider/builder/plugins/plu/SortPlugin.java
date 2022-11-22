package com.cn.jmw.data.provider.builder.plugins.plu;

import com.cn.jmw.data.provider.ThreadLocalCache;
import com.cn.jmw.data.provider.builder.packing.Bottle;
import com.cn.jmw.data.provider.builder.packing.Packing;
import com.cn.jmw.data.provider.builder.plugins.Plugins;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月09日 17:36
 * @Version 1.0
 */
public abstract class SortPlugin implements Plugins {

    @Override
    public abstract void append(ThreadLocalCache threadLocalCache);
}