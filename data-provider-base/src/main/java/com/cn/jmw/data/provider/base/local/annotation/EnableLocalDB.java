package com.cn.jmw.data.provider.base.local.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年10月23日 18:11
 * @Version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EnableLocalDBConfiguration.class)
public @interface EnableLocalDB {
}
