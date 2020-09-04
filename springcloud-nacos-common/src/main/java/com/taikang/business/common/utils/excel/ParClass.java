package com.taikang.business.common.utils.excel;

import com.taikang.business.common.utils.excel.annotation.ExcelHeader;
import com.taikang.business.common.utils.excel.annotation.ExcelResources;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 该类主要用于将带有注解的类内容封装到RowBean中
 */
public class ParClass {
    public static RowBean parCla2RowBean(Class clz) {
        RowBean rowBean = new RowBean();
        List<ExcelHeader> headers = new ArrayList<>();
        Method[] ms = clz.getDeclaredMethods();
        for (Method m : ms) {
            String mn = m.getName();
            if (mn.startsWith("get")) {
                if (m.isAnnotationPresent(ExcelResources.class)) {
                    ExcelResources er = m.getAnnotation(ExcelResources.class);
                    headers.add(new ExcelHeader(er.title(), er.order(), mn));
                }
            }
        }
        Collections.sort(headers);
        for (ExcelHeader header : headers) {
            rowBean.addCell(new CellBean(header.getTitle()));
        }
        return rowBean;
    }

}
