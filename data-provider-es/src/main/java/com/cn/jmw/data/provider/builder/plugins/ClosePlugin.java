package com.cn.jmw.data.provider.builder.plugins;


import lombok.Builder;
import lombok.Data;

import java.io.IOException;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月17日 22:44
 * @Version 1.0
 */
@Data
@Builder
public class ClosePlugin extends PluginsAbstract{
    @Override
    public void append() {
        try {
            esRequestParam.getRestHighLevelClient().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
