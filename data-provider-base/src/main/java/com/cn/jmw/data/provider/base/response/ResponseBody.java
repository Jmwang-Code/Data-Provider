package com.cn.jmw.data.provider.base.response;

import lombok.*;

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

    public Object status;

    public String body;

    public String errorMsg;
}
