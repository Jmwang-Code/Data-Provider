package com.cn.jmw.data.provider.base.entity.db;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jmw
 * @Description 数据库
 * @date 2022年10月17日 23:24
 * @Version 1.0
 */
@Data
public class Dataframes implements Serializable {

    private final String key;

    private final List<Dataframe> dataframes;

    /**
     * @Author jmw
     * @Description 初始化对象数据库
     * @Date 0:03 2022/10/18
     */
    private Dataframes(String key) {
        this.key = "DB" + key;
        dataframes = new ArrayList<>();
    }

    public static Dataframes of(String key, Dataframe... dataframes) {
        Dataframes df = new Dataframes(key);
        if (dataframes != null) {
            for (Dataframe dataframe : dataframes) {
                df.getDataframes().add(dataframe);
            }
        }
        return df;
    }

    /**
     * @Author jmw
     * @Description 数据量是否为空
     * @Date 0:02 2022/10/18
     */
    public boolean isEmpty() {
        return CollectionUtils.isEmpty(dataframes);
    }

    /**
     * @Author jmw
     * @Description 添加一个数据库
     * @Date 0:02 2022/10/18
     */
    public void add(Dataframe df) {
        dataframes.add(df);
    }

    /**
     * @Author jmw
     * @Description 获取数据库数量
     * @Date 0:02 2022/10/18
     */
    public int size() {
        return dataframes.size();
    }
}
