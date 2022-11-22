package com.cn.jmw.data.provider.builder.plugins.plu;

import com.cn.jmw.data.provider.ThreadLocalCache;
import com.cn.jmw.data.provider.base.entity.common.ValueType;
import com.cn.jmw.data.provider.base.entity.db.Column;
import com.cn.jmw.data.provider.base.entity.db.Dataframe;
import com.cn.jmw.data.provider.builder.Plugin;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月22日 10:50
 * @Version 1.0
 */
public class Analysis {


    public static void append(ThreadLocalCache threadLocalCache) {
        Dataframe dataframe = new Dataframe();
        SearchHits hits = ((SearchResponse) threadLocalCache.get(Plugin.SEARCH_RESPONSE)).getHits();
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
        threadLocalCache.put(Plugin.DATA_FRAME, dataframe);
    }
}
