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
package com.cn.jmw.data.provider.base.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author jmw
 * @Description 页面信息
 * @Date 23:44 2022/10/17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo implements Serializable {

    /**
     * 页数
     */
    private long pageSize;

    /**
     * 页码
     */
    private long pageNo;

    /**
     * 每页条数
     */
    private long total;

    /**
     * 总条数
     */
    private boolean countTotal;

    @Override
    public String toString() {
        return "PageInfo{" +
                "pageSize=" + pageSize +
                ", pageNo=" + pageNo +
                ", total=" + total +
                '}';
    }
}