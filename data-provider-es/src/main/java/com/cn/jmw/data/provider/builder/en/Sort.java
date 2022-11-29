package com.cn.jmw.data.provider.builder.en;

import org.elasticsearch.search.sort.SortOrder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月29日 18:03
 * @Version 1.0
 */
public enum Sort {

    ASC("ASC"),
    DESC("DESC");
    static Map<String, SortOrder> map = new HashMap();

    static {
        map.put("ASC",SortOrder.ASC);
        map.put("DESC",SortOrder.DESC);
    }
    private String string;

    Sort(String string){
        this.string = string;
    }

    public static SortOrder StringToSort(String string){
        return map.get(string);
    }
}
