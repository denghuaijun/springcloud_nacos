package com.taikang.business.common.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;

public class PropertyUtisEx extends BeanUtils {


    public static void copyProperties(Object dest, Object orig) {

        try {

            PropertyUtils.copyProperties(dest, orig);

        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

   /* static {

        ConvertUtils.register(new DateConvert(), java.util.Date.class);

        ConvertUtils.register(new DateConvert(), java.sql.Date.class);

        ConvertUtils.register(new BigDecimalConvert(), BigDecimal.class);

    }*/


}
