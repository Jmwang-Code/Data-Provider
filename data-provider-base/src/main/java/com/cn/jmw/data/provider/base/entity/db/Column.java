/*
 * Datart
 * <p>
 * Copyright 2021
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cn.jmw.data.provider.base.entity.db;

import com.cn.jmw.data.provider.base.entity.common.ValueType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author jmw
 * @Description 列
 * @Date 0:07 2022/10/18
 */
@Data
public class Column implements Serializable {

    private String[] name;

    /**
     * 值类型
     */
    private ValueType type;

    private String fmt;

    /**
     *  外键
     */
    private List<ForeignKey> foreignKeys;

    public Column(String[] name, ValueType type) {
        this.name = name;
        this.type = type;
    }

    public Column() {
    }

    public static Column of(ValueType type, String... names) {
        return new Column(names, type);
    }

    /**
     * 获取列名称
     */
    public String columnName() {
        return name[name.length - 1];
    }

    /**
     * 表名称
     */
    public String tableName() {
        if (name.length == 1) {
            return null;
        } else {
            return name[name.length - 2];
        }
    }

    /**
     * 列键位
     */
    public String columnKey() {
        return String.join(".", name);
    }

    public void setName(String... name) {
        this.name = name;
    }
}
