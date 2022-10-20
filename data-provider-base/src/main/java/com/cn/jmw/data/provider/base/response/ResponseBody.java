package com.cn.jmw.data.provider.base.response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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

    private Object status;

    private String body;

    private String errorMsg;

    public static String jsonConverter(Object o){
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(o);
        return jsonObject.toString();
    }
}
