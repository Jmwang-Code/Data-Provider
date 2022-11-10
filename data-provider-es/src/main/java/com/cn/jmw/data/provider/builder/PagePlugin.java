package com.cn.jmw.data.provider.builder;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月09日 17:35
 * @Version 1.0
 */
public abstract class PagePlugin implements Plugins {

    @Override
    public Packing packing() {
        return new Wrapper();
    }

    @Override
    public abstract float price();
}
