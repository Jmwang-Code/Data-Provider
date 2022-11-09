package com.cn.jmw.data.provider.es.elasticsearch.research;

import com.cn.jmw.data.provider.base.entity.common.ValueType;
import com.cn.jmw.data.provider.base.entity.db.Column;
import com.cn.jmw.data.provider.base.entity.db.Dataframe;
import com.cn.jmw.data.provider.es.entity.EsRequestParam;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author jmw
 * @Description TODO 精确查询
 * @date 2022年11月07日 14:32
 * @Version 1.0
 */
public class SearchReturn implements Return {

    private SearchRequest request = null;

    private SearchResponse response = null;

    @Override
    public Return init(EsRequestParam esRequestParam) {
        response = esRequestParam.getMode().initReturn(esRequestParam);
        return this;
    }

    @Override
    public Object start(Object o) {
        Dataframe dataframe = (Dataframe) o;
        SearchHits hits = response.getHits();

        dataframe.setColumns(new ArrayList<>());
        dataframe.setRows(new ArrayList<>());
        List<Column> columns = dataframe.getColumns();
        List<List<Object>> rows = dataframe.getRows();

        SearchHit at = hits.getAt(0);
        Map<String, Object> sourceAsMap1 = at.getSourceAsMap();
        Iterator<String> iterator1 = at.getSourceAsMap().keySet().iterator();
        while (iterator1.hasNext()) {
            //拿到key值
            String key = iterator1.next();
            Column column = new Column();
            column.setType(ValueType.STRING);
            column.setName(new String[]{key});
            columns.add(column);
        }

        for (SearchHit hit : hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            Iterator<String> iterator = hit.getSourceAsMap().keySet().iterator();
            List<Object> objects = new ArrayList<>();
            while (iterator.hasNext()) {
                //拿到key值
                String key = iterator.next();
                objects.add(sourceAsMap.get(key));
            }
            rows.add(objects);
        }
        return dataframe;
    }
}
