package com.cn.jmw.data.provider.builder.plugins.plu.page;

import com.cn.jmw.data.provider.builder.plugins.PagePlugin;
import com.cn.jmw.data.provider.es.entity.EsRequestParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月09日 17:36
 * @Version 1.0
 */
@Data
//@SuperBuilder
public class FromSizePage extends PagePlugin {

    private int from;
    private int size;

    @Builder
    public FromSizePage(int from,int size,EsRequestParam esRequestParam){
        super.esRequestParam = esRequestParam;
//        this.from = from;
//        this.size = size;
        this.from = esRequestParam.getPageStart();
        this.size = esRequestParam.getSize();
    }

    @Override
    public void append() {
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
    }

}