package com.mlzj.component.mq.client.config;

import com.mlzj.component.mq.client.annotation.MlzjMqConsumer;
import com.mlzj.component.mq.common.utils.CharacterUtils;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;


@Slf4j
@Component
public class MqBeanRegister implements BeanFactoryAware, BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

    private BeanFactory beanFactory;



    private ApplicationContext applicationContext;


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @SneakyThrows
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        //List<String> packageNames = AutoConfigurationPackages.get(beanFactory);
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(ComponentScan.class);
        String mainPackageName = this.getMainPackageName(beansWithAnnotation);
        List<Class<?>> hitClassList = this.getHitClassList(mainPackageName);
        for (Class<?> aClass : hitClassList) {
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(aClass);
            GenericBeanDefinition definition = (GenericBeanDefinition) beanDefinitionBuilder.getRawBeanDefinition();
            //设置beanDefinition中的属性值,例如clazz  设置为Dynamic.class
            //definition.getPropertyValues().add("clazz",aClass);
            //definition.setBeanClass(ProxyFactoryBean.class);
            definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
            definition.setScope(BeanDefinition.SCOPE_SINGLETON);
            beanDefinitionRegistry.registerBeanDefinition(CharacterUtils.toLowerCaseFirstOne(aClass.getSimpleName()), definition);
        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private String getMainPackageName(Map<String, Object> beansWithAnnotation) {
        for (Entry<String, Object> beanEntry : beansWithAnnotation.entrySet()) {
            Object value = beanEntry.getValue();
            try {
                Method main = value.getClass().getMethod("main", String[].class);
                return ClassUtils.getPackageName(value.getClass());
            } catch (NoSuchMethodException e) {
                log.info("can not find method for getMainPackageName");
            }
        }
        return "";
    }

    private List<Class<?>> getHitClassList(String mainPackageName) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                ClassUtils.convertClassNameToResourcePath(mainPackageName) + "/**/*.class";
        Resource[] resources = resourcePatternResolver.getResources(pattern);
        MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
        for (Resource resource : resources) {
            //用于读取类信息
            MetadataReader reader = readerFactory.getMetadataReader(resource);
            //扫描到的class
            String classname = reader.getClassMetadata().getClassName();
            Class<?> clazz = Class.forName(classname);
            MlzjMqConsumer annotation = clazz.getAnnotation(MlzjMqConsumer.class);
            if (annotation != null) {
                classes.add(clazz);
            }
        }
        return classes;
    }
}
