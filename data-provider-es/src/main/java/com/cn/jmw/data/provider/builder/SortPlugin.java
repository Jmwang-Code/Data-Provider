package com.cn.jmw.data.provider.builder;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月09日 17:36
 * @Version 1.0
 */
public abstract class SortPlugin implements Plugins {

    @Override
    public Packing packing() {
        return new Bottle();
    }

    @Override
    public abstract float price();
}