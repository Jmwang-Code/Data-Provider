package com.cn.jmw.data.provider.builder;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月09日 17:36
 * @Version 1.0
 */
public class VegBurger extends PagePlugin {

    @Override
    public float price() {
        return 25.0f;
    }

    @Override
    public String name() {
        return "Veg Burger";
    }
}