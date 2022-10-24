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

    /**
     * Response status
     */
    private Object status;

    /**
     * Response body Msg
     */
    private String body;

    /**
     * Response error Msg
     */
    private String errorMsg;

    /**
     * @Description Convert object to json string.
     * 
     * 
     * 
     * @Author jmw
     * @Date 15:56 2022/10/21
     */
    public static String jsonConverter(Object o){
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(o);
        return jsonObject.toString();
    }
}
