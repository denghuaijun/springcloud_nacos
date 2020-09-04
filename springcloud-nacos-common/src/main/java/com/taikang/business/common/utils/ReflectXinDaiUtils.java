package com.taikang.business.common.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReflectXinDaiUtils {
    /**
     * 得到属性值
     * @param obj
     */
    public static List readAttributeValue(Object obj){
        String nameVlues="";
        //得到class
        Class cls = obj.getClass();
        //得到所有属性
        Field[] fields = cls.getDeclaredFields();
        for (int i=0;i<fields.length;i++){//遍历
            try {
                //得到属性
                Field field = fields[i];
                //打开私有访问
                field.setAccessible(true);
                //获取属性值
                Object value = field.get(obj);
                //一个个赋值
                nameVlues += value+",";

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        //获取最后一个逗号的位置　　　　　　　
        int lastIndex = nameVlues.lastIndexOf(",");
        //不要最后一个逗号","
        String  result= nameVlues.substring(0,lastIndex);
        String[] split = result.split(",");
        List list=new ArrayList<>();
        for(String s:split){
            list.add(s);
        }

        return list;
    }

}
