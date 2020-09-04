package com.taikang.business.common.utils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

/**
 * @ClassName BeanConverter
 * @Description 对象转换工具类
 * @Author itw_denghj
 * @Date 2018/10/8
 * @Version 1.0
 */
public class BeanConverter {

    /**
     * 为对象copy提供的反射方法
     * @param source 源
     * @param target 目标
     * @param isCopyNull 如果是null要不要反射 true 反射 false 不反射
     * @throws Exception
     */
    public static void simpleCopyToBean(Object source ,Object target,Boolean isCopyNull) throws Exception{

        if (target == null || source == null) {
            return;
        }
        List targetMethodList = getSetter(target.getClass());
        List sourceMethodList = getGetter(source.getClass());
        Map<String, Method> map = new HashMap<String, Method>();

        Map<Class, String> supportTypeMap = new HashMap<Class, String>();
        supportTypeMap.put(Integer.class, "");
        supportTypeMap.put(Long.class, "");
        supportTypeMap.put(Double.class, "");
        supportTypeMap.put(int.class, "");
        supportTypeMap.put(long.class, "");
        supportTypeMap.put(double.class, "");
        supportTypeMap.put(BigDecimal.class, "");
        supportTypeMap.put(String.class, "");
        supportTypeMap.put(Boolean.class, "");
        supportTypeMap.put(boolean.class, "");
        supportTypeMap.put(byte[].class, "");
        supportTypeMap.put(Date.class, "");

        for (Iterator iter = sourceMethodList.iterator(); iter.hasNext();) {
            Method method = (Method) iter.next();
            map.put(method.getName().toUpperCase(), method);
        }
        for (Iterator iter = targetMethodList.iterator(); iter.hasNext();) {
            Method method = (Method) iter.next();
            String fieldName = method.getName().substring(3);
            try {
                Method sourceMethod = (Method) map.get("get".toUpperCase() + fieldName.toUpperCase());
                if (sourceMethod == null) {
                    sourceMethod = (Method) map.get("is".toUpperCase() + fieldName.toUpperCase());
                }
                if (sourceMethod == null) {
                    continue;
                }
                if (!supportTypeMap.containsKey(sourceMethod.getReturnType())) {
                    continue;
                }

                Object value = sourceMethod.invoke(source, new Object[0]);
                if (isCopyNull) {
                    if(value instanceof Integer || value instanceof Double){
                        if (method.getParameterTypes()[0].toString().indexOf("BigDecimal")>-1) {
                            value = new BigDecimal((value+""));
                        }
                        if(method.getParameterTypes()[0].toString().indexOf("Long")>-1){
                            value = new Long(new BigDecimal((value+"")).longValue());
                        }
                    }else if(value instanceof BigDecimal){
                        if(method.getParameterTypes()[0].toString().indexOf("Integer")>-1){
                            value = (new BigDecimal((value+""))).intValue();
                        }
                        if(method.getParameterTypes()[0].toString().indexOf("BigDecimal")>-1){
                            value = new BigDecimal((value+""));
                        }
                    }
                    method.invoke(target, new Object[] { value });
                } else {
                    if (value != null) {
                        method.invoke(target, new Object[] { value });
                    }
                }
            } catch (Exception e) {
                throw e;
            }
        }
    }


    /**
     * 为对象copy提供的反射方法
     * @param source 源
     * @param target 目标
     * @param isCopyNullAndBlank 如果是null  和 "" 要不要反射 true 反射 false 不反射
     * @throws Exception
     */
    public static void simpleCopyToBeanWithBlank(Object source ,Object target,Boolean isCopyNullAndBlank) throws Exception{

        if (target == null || source == null) {
            return;
        }
        List targetMethodList = getSetter(target.getClass());
        List sourceMethodList = getGetter(source.getClass());
        Map<String, Method> map = new HashMap<String, Method>();

        Map<Class, String> supportTypeMap = new HashMap<Class, String>();
        supportTypeMap.put(Integer.class, "");
        supportTypeMap.put(Long.class, "");
        supportTypeMap.put(Double.class, "");
        supportTypeMap.put(int.class, "");
        supportTypeMap.put(long.class, "");
        supportTypeMap.put(double.class, "");
        supportTypeMap.put(BigDecimal.class, "");
        supportTypeMap.put(String.class, "");
        supportTypeMap.put(Boolean.class, "");
        supportTypeMap.put(boolean.class, "");
        supportTypeMap.put(byte[].class, "");
        supportTypeMap.put(Date.class, "");

        for (Iterator iter = sourceMethodList.iterator(); iter.hasNext();) {
            Method method = (Method) iter.next();
            map.put(method.getName().toUpperCase(), method);
        }
        for (Iterator iter = targetMethodList.iterator(); iter.hasNext();) {
            Method method = (Method) iter.next();
            String fieldName = method.getName().substring(3);
            try {
                Method sourceMethod = (Method) map.get("get".toUpperCase() + fieldName.toUpperCase());
                if (sourceMethod == null || sourceMethod.equals("")) {
                    sourceMethod = (Method) map.get("is".toUpperCase() + fieldName.toUpperCase());
                }
                if (sourceMethod == null || sourceMethod.equals("") ) {
                    continue;
                }
                if (!supportTypeMap.containsKey(sourceMethod.getReturnType())) {
                    continue;
                }

                Object value = sourceMethod.invoke(source, new Object[0]);
                if (isCopyNullAndBlank) {
                    if(value instanceof Integer || value instanceof Double){
                        if (method.getParameterTypes()[0].toString().indexOf("BigDecimal")>-1) {
                            value = new BigDecimal((value+""));
                        }
                        if(method.getParameterTypes()[0].toString().indexOf("Long")>-1){
                            value = new Long(new BigDecimal((value+"")).longValue());
                        }
                    }else if(value instanceof BigDecimal){
                        if(method.getParameterTypes()[0].toString().indexOf("Integer")>-1){
                            value = (new BigDecimal((value+""))).intValue();
                        }
                        if(method.getParameterTypes()[0].toString().indexOf("BigDecimal")>-1){
                            value = new BigDecimal((value+""));
                        }
                    }
                    method.invoke(target, new Object[] { value });
                } else {
                    if (value != null && value != "") {
                        method.invoke(target, new Object[] { value });
                    }
                }
            } catch (Exception e) {
                throw e;
            }
        }
    }

    private static  List<Method> getSetter(Class cl) {
        List<Method> list = new ArrayList<Method>();
        Method[] methods = cl.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            String methodName = method.getName();
            if (!methodName.startsWith("set")) {
                continue;
            }
            list.add(method);
        }
        while (true) {
            cl = cl.getSuperclass();
            if (cl == Object.class) {
                break;
            }
            list.addAll(getSetter(cl));
        }
        return list;
    }

    private static List<Method> getGetter(Class cl) {
        List<Method> list = new ArrayList<Method>();
        Method[] methods = cl.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            String methodName = method.getName();
            if (!methodName.startsWith("get") && !methodName.startsWith("is")) {
                continue;
            }
            list.add(method);
        }
        while (true) {
            cl = cl.getSuperclass();
            if (cl == Object.class) {
                break;
            }
            list.addAll(getGetter(cl));
        }
        return list;
    }
}
