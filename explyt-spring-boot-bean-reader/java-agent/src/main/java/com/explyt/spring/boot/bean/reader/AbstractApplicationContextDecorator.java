/*
 * Copyright © 2024 Explyt Ltd
 *
 * All rights reserved.
 *
 * This code and software are the property of Explyt Ltd and are protected by copyright and other intellectual property laws.
 *
 * You may use this code under the terms of the Explyt Source License Version 1.0 ("License"), if you accept its terms and conditions.
 *
 * By installing, downloading, accessing, using, or distributing this code, you agree to the terms and conditions of the License.
 * If you do not agree to such terms and conditions, you must cease using this code and immediately delete all copies of it.
 *
 * You may obtain a copy of the License at: https://github.com/explyt/spring-plugin/blob/main/EXPLYT-SOURCE-LICENSE.md
 *
 * Unauthorized use of this code constitutes a violation of intellectual property rights and may result in legal action.
 */

package com.explyt.spring.boot.bean.reader;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.ResolvableType;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import tech.ytsaurus.spyt.patch.annotations.AddMethod;
import tech.ytsaurus.spyt.patch.annotations.Decorate;
import tech.ytsaurus.spyt.patch.annotations.DecoratedMethod;
import tech.ytsaurus.spyt.patch.annotations.OriginClass;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

import static com.explyt.spring.boot.bean.reader.Constants.*;

@Decorate
@OriginClass("org.springframework.context.support.AbstractApplicationContext")
public class AbstractApplicationContextDecorator {

    @DecoratedMethod
    protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        __registerBeanPostProcessors(beanFactory);

        explytPrintBeans(beanFactory);
        throw new RuntimeException(SPRING_EXPLYT_ERROR_MESSAGE);
    }

    protected void __registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
    }

    @AddMethod
    private void explytPrintBeans(ConfigurableListableBeanFactory beanFactory) {
        System.out.println(EXPLYT_BEAN_INFO_START);
        Map<String, String> beanTypeByNameMap = new TreeMap<>();
        String[] definitionNames = beanFactory.getBeanDefinitionNames();
        for (String beanName : definitionNames) {
            BeanDefinition definition = beanFactory.getBeanDefinition(beanName);

            boolean primary = definition.isPrimary();
            String className = definition.getBeanClassName();
            String scope = definition.getScope();
            scope = scope == null || scope.isEmpty() ? "singleton" : scope;
            String methodName = null;
            String methodType = null;

            String beanClassName = definition.getBeanClassName();
            String factoryBeanObjectType = explytGetFactoryBeanObjectType(definition);

            if (factoryBeanObjectType != null) {
                className = factoryBeanObjectType;
            } else if (beanClassName != null && definition instanceof AnnotatedBeanDefinition) {
                AnnotationMetadata metadata = ((AnnotatedBeanDefinition) definition).getMetadata();
                className = beanClassName;
                if (metadata != null && metadata.getClassName() != null) {
                    className = metadata.getClassName();
                }
            } else if (definition instanceof AnnotatedBeanDefinition
                    && ((AnnotatedBeanDefinition) definition).getFactoryMethodMetadata() != null) {
                MethodMetadata methodMetadata = ((AnnotatedBeanDefinition) definition).getFactoryMethodMetadata();
                className = methodMetadata.getDeclaringClassName();
                methodName = methodMetadata.getMethodName();
                methodType = methodMetadata.getReturnTypeName();
            } else if (definition instanceof RootBeanDefinition) {
                String springDataType = explytGetSpringDataType((RootBeanDefinition) definition);
                if (springDataType != null) {
                    className = springDataType;
                }
            }
            beanTypeByNameMap.put(beanName, methodType != null ? methodType : className);
            methodName = methodName == null ? "null" : "\"" + methodName + "\"";
            methodType = methodType == null ? "null" : "\"" + methodType + "\"";
            System.out.println(
                    EXPLYT_BEAN_INFO +
                            "{\"className\": \"" + className + "\"," +
                            "\"beanName\": \"" + beanName + "\"," +
                            "\"methodName\": " + methodName + "," +
                            "\"methodType\": " + methodType + "," +
                            "\"scope\": \"" + scope + "\"," +
                            "\"primary\": " + primary + "}"
            );
        }
        explytPrintAopData(beanFactory, beanTypeByNameMap);
        System.out.println(EXPLYT_BEAN_INFO_END);
    }

    @AddMethod
    public String explytGetFactoryBeanObjectType(BeanDefinition definition) {
        Object factoryBeanObjectType = definition.getAttribute("factoryBeanObjectType");
        if (factoryBeanObjectType instanceof String) {
            return (String) factoryBeanObjectType;
        } else if (factoryBeanObjectType instanceof Class) {
            return ((Class<?>) factoryBeanObjectType).getName();
        }
        return null;
    }

    @AddMethod
    public String explytGetSpringDataType(RootBeanDefinition definition) {
        try {
            Class<?> targetType = definition.getTargetType();
            if (targetType != null && targetType.getName().contains(".data")) {
                ResolvableType resolvableType = definition.getResolvableType();
                Class<?> baseDataRepoFactoryClass = Class.forName(
                        "org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport"
                );
                if (baseDataRepoFactoryClass.isAssignableFrom(targetType)) {
                    ResolvableType generic = resolvableType.getGeneric(0);
                    return generic.toClass().getName();
                }
            }
            return null;
        } catch (Throwable ignore) {
            return null;
        }
    }

    @AddMethod
    private static void explytPrintAopData(ConfigurableListableBeanFactory beanFactory, Map<String, String> map) {
        try {
            Class<?> aopReaderClass = Class.forName("org.springframework.aop.aspectj.AspectJAopUtils");
            Method[] methods = aopReaderClass.getMethods();
            for (Method method : methods) {
                if ("explytPrintAopData".equals(method.getName())) {
                    method.invoke(null, beanFactory, map);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

