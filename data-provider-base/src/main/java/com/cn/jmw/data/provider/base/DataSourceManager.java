package com.cn.jmw.data.provider.base;

import com.cn.jmw.data.provider.base.response.ResponseBody;

/**
 * @author jmw
 * @Description 实现接口
 * @date 2022年10月08日 18:10
 * @Version 1.0
 */
interface DataSourceManager {

    /**
     * @param t:未指定pojo
     * @return void
     * @Author jmw
     * @Description 测试数据源连接方法
     * 解决各种连接测试问题
     * @Date 18:13 2022/10/8
     */
    <T> ResponseBody testConnection(T t);

    /**
     * @Author jmw
     * @Description ping连接方法针对于虚拟机服务器等物理机
     * @param t:未指定pojo
     * @return boolean
     * @Date 18:15 2022/10/8
     */
//    <T> boolean ping(T t);

    /**
     * @Author jmw
     * @Description 各种准备执行逻辑
     * @param a:入参对象
     * @return T 出参对象
     * @Date 18:20 2022/10/8
     */
//    <T,A> T execute(A a);


}
