package com.taikang.business.common.utils.excel;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 表bean
 * Created by lee on 2016/11/26.
 */
@Data
public class SheetBean implements Serializable {

    private static final long serialVersionUID = 4512511036918724645L;
    private List<RowBean> rowList;

    private String value;

    public SheetBean() {
        rowList = new ArrayList<RowBean>();
    }

    public RowBean addRow() {
        RowBean rowBean = new RowBean();
        rowList.add(rowBean);
        return rowBean;
    }

    public void addRow(RowBean rowBean) {
        rowList.add(rowBean);
    }

}
