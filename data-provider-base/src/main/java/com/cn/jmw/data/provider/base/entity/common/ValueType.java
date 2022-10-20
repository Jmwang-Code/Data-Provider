package com.cn.jmw.data.provider.base.entity.common;

public enum ValueType {

    STRING,

    NUMERIC,

    DATE,

    BOOLEAN,

    IDENTIFIER,

    FRAGMENT, //  do nothing

    SNIPPET, //will be parse to sql node

    KEYWORD,
}
