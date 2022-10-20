package com.cn.jmw.data.provider.test.execute;

import com.cn.jmw.data.provider.base.OuterDataSourceManager;
import org.junit.Test;

import java.util.HashMap;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年10月20日 18:28
 * @Version 1.0
 */
public class JdbcExecute {

    static OuterDataSourceManager outerDataSourceManager = new OuterDataSourceManager();

    static HashMap map = new HashMap();
    static {
        map.put("dbType","MYSQL");
        map.put("driverClass","com.mysql.cj.jdbc.Driver");
        map.put("url","jdbc:mysql://152.136.154.249:3306/demo?&allowMultiQueries=true&characterEncoding=utf-8");
        map.put("user","demo");
        map.put("password","kKJ8XynXLzjfYDA7");
    }


}
