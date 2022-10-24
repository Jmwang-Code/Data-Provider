package com.cn.jmw.data.provider.localdb.annotation;

import com.cn.jmw.data.provider.base.utils.StartLogPrinting;
import com.cn.jmw.data.provider.localdb.LocalPersistentDB;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年10月23日 18:13
 * @Version 1.0
 */
public class EnableLocalDBConfiguration implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(EnableLocalDBConfiguration.class);
        builder.setScope(BeanDefinition.SCOPE_SINGLETON);
        registry.registerBeanDefinition("LocalDB", builder.getBeanDefinition());
        LocalPersistentDB.init();
        StartLogPrinting.SingletonEnum.SINGLETON.getInstance()
                .add("initialization LOCALDB")
                .build();
    }

}
