package com.cn.jmw.data.provider;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年11月18日 11:59
 * @Version 1.0
 */
public class ThreadLocalCache {

    /**
     * 实例字段，每个线程一个store，每个线程生产一个{@code ThreadLocalCache} INSTANCE
     */
    private final ThreadLocal<Map<Object, Object>> store;

    public ThreadLocalCache() {
        this.store = new ThreadLocal<Map<Object, Object>>() {
            @Override
            protected Map<Object, Object> initialValue() {
                return new HashMap<Object, Object>();
            }
        };
    }

    public ThreadLocalCache put(Object key, Object value) {
        store.get().put(key, value);
        return this;
    }

    public void build(){}

    public Object get(Object key) {
        //TODO反射做控制
        return store.get().get(key);
    }

    public void close(){
        this.store.remove();
    }
}
