package com.cn.jmw.data.provider.builder.plugins.plu.sort;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月09日 17:37
 * @Version 1.0
 */
//public class Descending extends SortPlugin {
//
//    private EsRequestParam esRequestParam;
//
//    private SearchSourceBuilder searchSourceBuilder;
//
//    private SortOrder sort;
//
//    private String sortName;
//
//    public Descending(ThreadLocalCache threadLocalCache) {
//        esRequestParam = (EsRequestParam)threadLocalCache.get(Plugin.ES_REQUEST_PARAM);
//        searchSourceBuilder = (SearchSourceBuilder)threadLocalCache.get(Plugin.SEARCH_SOURCE_BUILDER);
//        this.sort = Sort.StringToSort(esRequestParam.getSort());
//        this.sortName = esRequestParam.getSortName();
//    }
//
//    @Override
//    public void append() {
//        if (sort!=null){
//            if (StringUtils.isNotBlank(sortName))searchSourceBuilder.sort(sortName,sort);
//            else searchSourceBuilder.sort(sortName);
//        }
//    }
//
//}