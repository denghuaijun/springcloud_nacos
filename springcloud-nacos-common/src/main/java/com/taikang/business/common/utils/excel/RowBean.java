package com.taikang.business.common.utils.excel;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 行bean
 */
public class RowBean implements Serializable {

    private static final long serialVersionUID = -6667319257164445219L;
    @Getter
    @Setter
    private List<CellBean> cellList;

    private Boolean Merge = false;

    public RowBean() {
        cellList = new ArrayList<CellBean>();
    }

    public RowBean addCell(CellBean cellBean) {
        cellList.add(cellBean);
        return this;
    }

    public void changeMerge() {
        this.Merge = true;
    }

    public Boolean isMerge() {
        return Merge;
    }

}