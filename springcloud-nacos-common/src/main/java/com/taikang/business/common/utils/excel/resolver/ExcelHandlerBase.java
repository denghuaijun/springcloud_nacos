package com.taikang.business.common.utils.excel.resolver;


import com.taikang.business.common.utils.excel.ExcelBean;
import com.taikang.business.common.utils.excel.ExcelHandler;

/**
 * Created by libin on 2017/3/17.
 */
public abstract class ExcelHandlerBase implements ExcelHandler {

    protected boolean wrap = false;//自动换行

    protected void initConfigure(ExcelBean excelBean) {
        //设置自动换行
        if (excelBean != null) {
            wrap = excelBean.isWrap();
        }
    }
}
