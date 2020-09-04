package com.taikang.business.common.utils.excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;

/**
 * Created by libin on 2017/3/17.
 */
public interface ExcelHandler {

    byte[] createExcel(ExcelBean excelBean) throws IOException;

    ExcelBean parseExcel(byte[] bytes, boolean isSingleString) throws IOException, InvalidFormatException;
}
