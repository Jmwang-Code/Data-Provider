package com.cn.jmw.data.provider.builder.plugins.plu;

import com.cn.jmw.data.provider.builder.plugins.PagePlugin;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月09日 17:37
 * @Version 1.0
 */
public class ScrollPage extends PagePlugin {

    @Override
    public float price() {
        return 50.5f;
    }

    @Override
    public String name() {
        return "Chicken Burger";
    }
}