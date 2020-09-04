package com.taikang.business.common.utils.excel;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 单元格Bean
 */
@Data
public class CellBean implements Serializable {

    private static final long serialVersionUID = 4297269195062053587L;
    private Date valueByDate;
    private String dateFormat;
    private String valueByString;
    private Double valueByDouble;
    private Boolean valueByBoolean;
    private Integer valueByInteger;
    private int cellType;
    private Integer startCol;
    private Integer endCol;

    public CellBean() {
    }

    public CellBean(String valueByString) {
        this.valueByString = valueByString;
    }

    public CellBean(Date date, String dateFormat) {
        this.valueByDate = date;
        this.dateFormat = dateFormat;
    }

    public CellBean(Double valueByDouble) {
        this.valueByDouble = valueByDouble;
    }

    public CellBean(Boolean valueByBoolean) {
        this.valueByBoolean = valueByBoolean;
    }

    public CellBean(Integer valueByInteger) {
        this.valueByInteger = valueByInteger;
    }

    public String getValue() {
        if (valueByString != null) {
            return valueByString;
        } else if (valueByDouble != null) {
            return subZeroAndDot(new BigDecimal(valueByDouble).toString());
        } else if (valueByBoolean != null) {
            return valueByBoolean.toString();
        } else if (valueByInteger != null) {
            return valueByInteger.toString();
        } else if (valueByDate != null) {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sf.format(valueByDate);
        } else {
            return "";
        }
    }

    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");
            s = s.replaceAll("[.]$", "");
        }

        return s;
    }

}