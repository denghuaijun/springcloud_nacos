package com.taikang.business.common.utils.excel.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 用来在对象的get方法上加入的annotation,通过该注解说明某个属性对应的标题
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelResources {
    /**
     * 属性的标题名称
     */
    String title();

    /**
     * 在excel的顺序
     *
     * @return
     */
    int order() default 9999;
}
