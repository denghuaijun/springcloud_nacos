package com.taikang.business.common.utils;


import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;

import java.util.List;

/**
 * @author liuyf104
 */
public class BeanUtils {

    public static void copyProperties(Object source, Object target) {
        BeanCopier copier = BeanCopier.create(source.getClass(), target.getClass(), false);
        copier.copy(source, target, null);
    }

    public static void copyProperties(Object source, Object target, Converter converter) {
        BeanCopier copier = BeanCopier.create(source.getClass(), target.getClass(), true);
        copier.copy(source, target, converter);
    }

    /**
     * 批量拷贝集合的方法
     *
     * @param sourceList
     * @param targetList
     * @param clasz
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static <T, V> List<V> copyPropertieses(List<T> sourceList, List<V> targetList, Class<?> clasz) {
        try {
            if (sourceList != null && sourceList.size() >= 1) {
                for (T t : sourceList) {
                    V instance = (V) clasz.newInstance();
                    copyProperties(t, instance);
                    targetList.add(instance);
                }
                return targetList;
            }
            return targetList;
        } catch (Exception e) {
            return null;
        }
    }

}
