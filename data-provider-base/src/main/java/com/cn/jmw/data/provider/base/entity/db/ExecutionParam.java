package com.cn.jmw.data.provider.base.entity.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年10月20日 18:00
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecutionParam {

    @Builder.Default
    private String sql = "SELECT 1 FROM DUAL";
}
