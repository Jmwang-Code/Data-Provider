package com.cn.jmw.data.provider.base;

import com.cn.jmw.data.provider.base.bean.DataProviderSource;
import com.cn.jmw.data.provider.base.response.ResponseBody;

/**
 * @author jmw
 * @Description Provider模式顶级管理者 —— 实现(具体方法行为)
 * @date 2022年10月08日 18:10
 * @Version 1.0
 */
interface DataSourceManager {

    /**
     * @Author jmw
     * @Description 测试数据源连接方法
     * 解决各种连接测试问题
     * @Date 18:13 2022/10/8
     */
    ResponseBody testConnection(DataProviderSource t) throws Exception;

    /**
     * @Author jmw
     * @Description ping连接方法针对于虚拟机服务器等物理机
     * @Date 18:15 2022/10/8
     */
//    <T> boolean ping(T t);

    /**
     * @Author jmw
     * @Description 各种准备执行逻辑
     * @Date 18:20 2022/10/8
     */
//    <T,A> T execute(A a);


}
