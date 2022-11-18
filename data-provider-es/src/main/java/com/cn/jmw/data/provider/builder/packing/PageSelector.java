package com.cn.jmw.data.provider.builder.packing;

import com.cn.jmw.data.provider.builder.plugins.Plugins;
import com.cn.jmw.data.provider.builder.plugins.plu.page.FromSizePage;
import com.cn.jmw.data.provider.es.entity.EsRequestParam;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月09日 17:35
 * @Version 1.0
 */
public class PageSelector implements Packing {

    @Override
    public Plugins selecting() {
        //具体选择某一个分页模式
        Plugins pagePlugin = null;
        return pagePlugin;
    }
}