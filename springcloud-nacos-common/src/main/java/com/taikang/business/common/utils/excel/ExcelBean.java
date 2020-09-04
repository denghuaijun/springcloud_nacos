package com.taikang.business.common.utils.excel;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ExcelBean
 * Created by lee on 2016/11/26.
 */
@Data
public class ExcelBean implements Serializable {

    private static final long serialVersionUID = 1776188080397849282L;

    public List<SheetBean> sheetList;

    public ExcelBean() {
        this.sheetList = new ArrayList<SheetBean>();
    }

    public SheetBean addSheet(String sheetName) {
        SheetBean sheetBean = new SheetBean();
        sheetBean.setValue(sheetName);
        sheetList.add(sheetBean);
        return sheetBean;
    }

    /**
     * 自动换行
     */
    boolean wrap;

}
