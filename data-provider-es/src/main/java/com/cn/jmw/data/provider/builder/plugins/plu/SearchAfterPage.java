package com.cn.jmw.data.provider.builder.plugins.plu;

import com.cn.jmw.data.provider.builder.plugins.PagePlugin;
import lombok.Builder;
import lombok.Data;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月09日 17:36
 * @Version 1.0
 */
@Data
@Builder
public class SearchAfterPage extends PagePlugin {

    private int from;

    @Override
    public float build() {
        return 25.0f;
    }

}