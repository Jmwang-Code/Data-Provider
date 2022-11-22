package com.cn.jmw.data.provider.builder.plugins.plu;


import com.cn.jmw.data.provider.builder.Plugin;
import com.cn.jmw.data.provider.ThreadLocalCache;
import com.cn.jmw.data.provider.builder.plugins.Plugins;
import com.cn.jmw.data.provider.es.entity.EsRequestParam;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月17日 22:44
 * @Version 1.0
 */
@Data
@AllArgsConstructor
public class ClosePlugin implements Plugins {

    private ThreadLocalCache threadLocalCache;

    @Override
    public void append(ThreadLocalCache threadLocalCache) {
        EsRequestParam esRequestParam = (EsRequestParam)threadLocalCache.get(Plugin.ES_REQUEST_PARAM);
        try {
            esRequestParam.getRestHighLevelClient().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
