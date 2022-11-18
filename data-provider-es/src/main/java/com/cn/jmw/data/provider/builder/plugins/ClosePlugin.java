package com.cn.jmw.data.provider.builder.plugins;


import com.cn.jmw.data.provider.ThreadLocalCache;
import com.cn.jmw.data.provider.es.entity.EsRequestParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月17日 22:44
 * @Version 1.0
 */
@Data
@AllArgsConstructor
public class ClosePlugin implements Plugins{

    private ThreadLocalCache threadLocalCache;

    @Override
    public void append() {
        EsRequestParam esRequestParam = (EsRequestParam)threadLocalCache.get("esRequestParam");
        try {
            esRequestParam.getRestHighLevelClient().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
