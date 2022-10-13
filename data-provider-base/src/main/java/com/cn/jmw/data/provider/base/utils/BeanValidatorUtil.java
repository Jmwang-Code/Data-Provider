package com.cn.jmw.data.provider.base.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.StringJoiner;

/**
 * @author jmw
 * @Description Bean验证器工具
 * @date 2022年10月13日 10:41
 * @Version 1.0
 */
@Slf4j
public class BeanValidatorUtil {

    private static final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public static void validate(Object obj, Class<?>... groups) {
        Set<ConstraintViolation<Object>> validate = validatorFactory.getValidator().validate(obj, groups);
        if (!CollectionUtils.isEmpty(validate)) {
            StringJoiner message = new StringJoiner(",");
            for (ConstraintViolation<Object> v : validate) {
                message.add(v.getPropertyPath() + ":" + v.getMessage());
            }
            log.info(message.toString());
        }
    }
}
