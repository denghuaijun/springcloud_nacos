package com.taikang.business.common.utils.excel.annotation;

import lombok.Data;

/**
 * 表头信息
 */
@Data
public class ExcelHeader implements Comparable<ExcelHeader> {
    /**
     * excelde 标题名称
     */
    private String title;

    /**
     * 每一个标题的顺序
     */
    private int order;
    /**
     * 说对应的方法
     */
    private String methodName;

    public ExcelHeader(String title, int order, String methodName) {
        super();
        this.title = title;
        this.order = order;
        this.methodName = methodName;
    }

    public String getTitle() {
        return title;
    }

    public int getOrder() {
        return order;
    }

    public String getMethodName() {
        return methodName;
    }

    @Override
    public int compareTo(ExcelHeader o) {
        return order > o.order ? 1 : (order < o.order ? -1 : 0);
    }
}
