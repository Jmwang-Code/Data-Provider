package com.cn.jmw.data.provider.base.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年10月08日 18:40
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBody {

    public String body;
}
