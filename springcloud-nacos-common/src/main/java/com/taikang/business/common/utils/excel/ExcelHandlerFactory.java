package com.taikang.business.common.utils.excel;

import com.taikang.business.common.utils.excel.resolver.ExcelHandlerHSSF;
import com.taikang.business.common.utils.excel.resolver.ExcelHandlerXSSF;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by libin on 2017/3/17.
 */
public class ExcelHandlerFactory {

    public static byte[] create2003Excel(ExcelBean excelBean) throws IOException {
        ExcelHandler excelHandler = new ExcelHandlerHSSF();
        return excelHandler.createExcel(excelBean);
    }

    public static ExcelBean parse2003Excel(byte[] bytes) throws IOException, InvalidFormatException {
        ExcelHandler excelHandler = new ExcelHandlerHSSF();
        return excelHandler.parseExcel(bytes, false);
    }

    public static ExcelBean parse2003Excel(InputStream inputStream) throws IOException, InvalidFormatException {
        ExcelHandler excelHandler = new ExcelHandlerHSSF();
        return excelHandler.parseExcel(input2byte(inputStream), false);
    }

    public static ExcelBean parse2003Excel(byte[] bytes, boolean isSingleString) throws IOException, InvalidFormatException {
        ExcelHandler excelHandler = new ExcelHandlerHSSF();
        return excelHandler.parseExcel(bytes, isSingleString);
    }

    public static ExcelBean parse2003Excel(InputStream inputStream, boolean isSingleString) throws IOException, InvalidFormatException {
        ExcelHandler excelHandler = new ExcelHandlerHSSF();
        return excelHandler.parseExcel(input2byte(inputStream), isSingleString);
    }

    public static byte[] create2007Excel(ExcelBean excelBean) throws IOException {
        ExcelHandler excelHandler = new ExcelHandlerXSSF();
        return excelHandler.createExcel(excelBean);
    }

    public static ExcelBean parse2007Excel(byte[] bytes) throws IOException, InvalidFormatException {
        ExcelHandler excelHandler = new ExcelHandlerXSSF();
        return excelHandler.parseExcel(bytes, false);
    }

    public static ExcelBean parse2007Excel(InputStream inputStream) throws IOException, InvalidFormatException {
        ExcelHandler excelHandler = new ExcelHandlerXSSF();
        return excelHandler.parseExcel(input2byte(inputStream), false);
    }

    public static ExcelBean parse2007Excel(byte[] bytes, boolean isSingleString) throws IOException, InvalidFormatException {
        ExcelHandler excelHandler = new ExcelHandlerXSSF();
        return excelHandler.parseExcel(bytes, isSingleString);
    }

    public static ExcelBean parse2007Excel(InputStream inputStream, boolean isSingleString) throws IOException, InvalidFormatException {
        ExcelHandler excelHandler = new ExcelHandlerXSSF();
        return excelHandler.parseExcel(input2byte(inputStream), isSingleString);
    }

    public static final byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }

}
