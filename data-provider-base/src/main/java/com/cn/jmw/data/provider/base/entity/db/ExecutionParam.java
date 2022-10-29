package com.cn.jmw.data.provider.base.entity.db;

import com.cn.jmw.data.provider.base.entity.PageInfo;
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

    private PageInfo pageInfo;

    /**
     * @Description The number of pages is empty.
     * 
     * 
     * 
     * @Author jmw
     * @Date 19:06 2022/10/25
     */
    public static ExecutionParam empty() {
        ExecutionParam executeParam = new ExecutionParam();
        executeParam.setPageInfo(PageInfo.builder().pageNo(1).pageSize(Integer.MAX_VALUE).build());
        return executeParam;
    }

    private String sql;

    private String tableName;
}
