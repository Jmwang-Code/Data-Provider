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
package com.cn.jmw.data.provider.base.utils;

import com.cn.jmw.data.provider.base.calcite.dialect.CustomSqlTypeName;
import com.cn.jmw.data.provider.base.calcite.dialect.JavaType;
import com.cn.jmw.data.provider.base.entity.common.ValueType;
import org.apache.calcite.sql.type.SqlTypeFamily;
import org.apache.calcite.sql.type.SqlTypeName;

import java.sql.Types;
import java.util.Date;

public class DataTypeUtils {

    /**
     * @Description Sql type parser
     *
     *
     *
     * @Author jmw
     * @Date 16:17 2022/10/21
     */
    public static ValueType sqlType2DataType(String sqlType) {
        sqlType = sqlType.toUpperCase();
        SqlTypeName sqlTypeName = SqlTypeName.get(sqlType);
        SqlTypeFamily family;
        if (sqlTypeName == null) {
            family = CustomSqlTypeName.SQL_TYPE_FAMILY_MAP.getOrDefault(sqlType, CustomSqlTypeName.ANY).getFamily();
        } else {
            family = sqlTypeName.getFamily();
        }
        switch (family) {
            case NUMERIC:
                return ValueType.NUMERIC;
            case DATE:
            case TIME:
            case TIMESTAMP:
            case DATETIME:
                return ValueType.DATE;
            default:
                return ValueType.STRING;
        }
    }

    public static SqlTypeName javaType2SqlType(String javaTypeSimpleName) {
        JavaType javaType = JavaType.valueOf(javaTypeSimpleName.toUpperCase());
        switch (javaType) {
            case BYTE:
            case SHORT:
            case INTEGER:
            case LONG:
            case FLOAT:
            case DOUBLE:
                return SqlTypeName.DOUBLE;
            case DATE:
                return SqlTypeName.DATE;
            case BOOLEAN:
                return SqlTypeName.BOOLEAN;
            default:
                return SqlTypeName.VARCHAR;
        }
    }

    public static ValueType javaType2DataType(Object obj) {
        if (obj instanceof Number) {
            return ValueType.NUMERIC;
        } else if (obj instanceof Date) {
            return ValueType.DATE;
        } else if (obj instanceof Boolean) {
            return ValueType.BOOLEAN;
        } else {
            return ValueType.STRING;
        }
    }

    public static SqlTypeName javaType2SqlType(ValueType valueType) {
        switch (valueType) {
            case NUMERIC:
                return SqlTypeName.DOUBLE;
            case DATE:
                return SqlTypeName.DATE;
            case BOOLEAN:
                return SqlTypeName.BOOLEAN;
            default:
                return SqlTypeName.VARCHAR;
        }
    }

    public static int valueType2SqlTypes(ValueType valueType) {
        switch (valueType) {
            case NUMERIC:
                return Types.DOUBLE;
            case DATE:
                return Types.TIMESTAMP;
            case BOOLEAN:
                return Types.BOOLEAN;
            default:
                return Types.VARCHAR;
        }
    }


}
