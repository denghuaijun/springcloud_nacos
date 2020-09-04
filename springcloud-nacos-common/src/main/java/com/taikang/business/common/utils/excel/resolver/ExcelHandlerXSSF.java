package com.taikang.business.common.utils.excel.resolver;

import com.taikang.business.common.utils.excel.*;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by libin on 2017/3/17.
 */
public class ExcelHandlerXSSF extends ExcelHandlerBase implements ExcelHandler {

    private XSSFWorkbook xssfWorkbook = null;

    private void setCellValue(XSSFCell cell, CellBean cellBean, CellStyle cellStyle) {
        CreationHelper createHelper = xssfWorkbook.getCreationHelper();
        if (cellBean.getValueByDate() != null) {
            CellStyle celle = xssfWorkbook.createCellStyle();
            String dateformat = "m/d/yy h:mm";
            if (cellBean.getDateFormat() != null) {
                dateformat = cellBean.getDateFormat();
            }
            cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(dateformat));
            cell.setCellValue(cellBean.getValueByDate());
            cell.setCellStyle(cellStyle);
        } else if (cellBean.getValueByString() != null) {
            cell.setCellValue(createHelper.createRichTextString(cellBean.getValueByString()));
        } else if (cellBean.getValueByDouble() != null) {
            cell.setCellValue(cellBean.getValueByDouble() + "");
        } else if (cellBean.getValueByBoolean() != null) {
            cell.setCellValue(cellBean.getValueByBoolean());
        } else if (cellBean.getValueByInteger() != null) {
            cell.setCellValue(cellBean.getValueByInteger());
        }

        cellStyle.setWrapText(wrap);
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//垂直
        cell.setCellStyle(cellStyle);

    }

    @Override
    public byte[] createExcel(ExcelBean excelBean) throws IOException {
        initConfigure(excelBean);
        xssfWorkbook = new XSSFWorkbook();
        CellStyle cellStyle = getCellStyle(xssfWorkbook);
        for (SheetBean sheetBean : excelBean.getSheetList()) {
            String safeName = WorkbookUtil.createSafeSheetName(sheetBean.getValue());
            XSSFSheet XSSFsheet = xssfWorkbook.createSheet(safeName);
            short rowIndex = 0;
            for (RowBean rowBean : sheetBean.getRowList()) {
                XSSFRow XSSFrow = XSSFsheet.createRow(rowIndex);
                int cellIndex = 0;
                if (rowBean.isMerge()) {
                    CellBean cellBean = rowBean.getCellList().get(0);
                    XSSFsheet.addMergedRegion(new CellRangeAddress(0, 0, cellBean.getStartCol(), cellBean.getEndCol()));
                    CellStyle celle = xssfWorkbook.createCellStyle();
                    celle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
                    XSSFCell cell = XSSFrow.createCell(cellIndex);
                    setCellValue(cell, cellBean, celle);
                } else {
                    for (CellBean cellBean : rowBean.getCellList()) {
                        XSSFCell cell = XSSFrow.createCell(cellIndex);
                        setCellValue(cell, cellBean, cellStyle);
                        cellIndex++;
                    }
                }
                rowIndex++;
            }
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        xssfWorkbook.write(out);
        return out.toByteArray();
    }

    private CellStyle getCellStyle(XSSFWorkbook xSSFWorkbook) {
        XSSFCellStyle cellStyle = xSSFWorkbook.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);
        cellStyle.setWrapText(wrap);
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        XSSFFont contentFont = getContentFont(xssfWorkbook);
        // 把字体应用到当前的样式
        cellStyle.setFont(contentFont);
        return cellStyle;
    }

    private XSSFFont getContentFont(XSSFWorkbook xSSFWorkbook) {
        // 生成另一个字体
        XSSFFont font = xSSFWorkbook.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
        return font;
    }

    @Override
    public ExcelBean parseExcel(byte[] bytes, boolean isSingleString) throws IOException, InvalidFormatException {
        ExcelBean excelBean = new ExcelBean();
        InputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Workbook wb = WorkbookFactory.create(byteArrayInputStream);
        int numberOfSheets = wb.getNumberOfSheets();

        List<SheetBean> sheetBeans = new ArrayList<>();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = wb.getSheetAt(i);
            SheetBean sheetBean = new SheetBean();
            sheetBean.setValue(sheet.getSheetName());

            Iterator<Row> rowIterator = sheet.iterator();
            List<RowBean> rowBeens = new ArrayList<>();
            sheetBean.setRowList(rowBeens);
            sheetBeans.add(sheetBean);

            while (rowIterator.hasNext()) {
                RowBean rowBean = new RowBean();
                rowBeens.add(rowBean);
                Row row = rowIterator.next();
                List<CellBean> cellBeens = new ArrayList<>();
                rowBean.setCellList(cellBeens);
                short lastCellNum = row.getLastCellNum();
                for (int celli = 0; celli < lastCellNum; celli++) {
                    Cell cell = row.getCell(celli);
                    CellBean cellBean = null;
                    if (cell != null) {
                        int cellType = cell.getCellType();
                        cellBean = new CellBean();
                        if (cellType == XSSFCell.CELL_TYPE_STRING) {
                            cellBean = new CellBean(cell.getStringCellValue());
                        } else if (cellType == XSSFCell.CELL_TYPE_NUMERIC) {
                            if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                                double value = cell.getNumericCellValue();
                                Date date = DateUtil.getJavaDate(value);
                                cellBean = new CellBean();
                                cellBean.setValueByDate(date);
                            } else {
                                cellBean = new CellBean(cell.getNumericCellValue());
                            }
                        }
                        if (cellBean.getValueByDate() != null) {
                            cellBean.setCellType(6);
                        } else {
                            cellBean.setCellType(cellType);
                        }
                    }

                    cellBeens.add(cellBean);
                }
            }

        }
        excelBean.setSheetList(sheetBeans);
        return excelBean;
    }
}
