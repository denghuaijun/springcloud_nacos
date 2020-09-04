package com.taikang.business.common.utils.excel.resolver;

import com.taikang.business.common.utils.excel.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.WorkbookUtil;

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
public class ExcelHandlerHSSF extends ExcelHandlerBase implements ExcelHandler {

    private HSSFWorkbook hssfWorkbook = null;

    @Override
    public byte[] createExcel(ExcelBean excelBean) throws IOException {
        initConfigure(excelBean);
        hssfWorkbook = new HSSFWorkbook();
        CellStyle cellStyle = getCellStyle(hssfWorkbook);
        for (SheetBean sheetBean : excelBean.getSheetList()) {
            String safeName = WorkbookUtil.createSafeSheetName(sheetBean.getValue());
            HSSFSheet sheet = hssfWorkbook.createSheet(safeName);
            short rowIndex = 0;
            List<Integer> list = new ArrayList<>();
            for (RowBean rowBean : sheetBean.getRowList()) {
                HSSFRow row = sheet.createRow(rowIndex);
                int cellIndex = 0;
                if (rowBean.isMerge()) {
                    CellBean cellBean = rowBean.getCellList().get(0);
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, cellBean.getStartCol(), cellBean.getEndCol()));
                    HSSFCellStyle celle = hssfWorkbook.createCellStyle();
                    celle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    HSSFCell cell = row.createCell(cellIndex);
                    setCellValue(cell, cellBean, celle);
                } else {
                    for (CellBean cellBean : rowBean.getCellList()) {
                        Integer aLong = null;
                        if (list.size() > cellIndex) {
                            aLong = list.get(cellIndex);
                        }
                        if (StringUtils.isNotBlank(cellBean.getValueByString())) {
                            if (aLong == null) {
                                int len = cellBean.getValueByString().getBytes().length * 256;
                                list.add(len > '\uff00' ? '\uff00' : len);
                            } else {
                                Integer len = cellBean.getValueByString().getBytes().length * 256;
                                if (aLong.compareTo(len) < -1) {
                                    list.set(cellIndex, len > '\uff00' ? '\uff00' : len);
                                }
                            }
                        }
                        HSSFCell cell = row.createCell(cellIndex);
                        setCellValue(cell, cellBean, cellStyle);
                        cellIndex++;
                    }
                }
                rowIndex++;
            }
            for (int i = 0; i < list.size(); i++) {
                sheet.autoSizeColumn(i, true);
                //sheet.setColumnWidth(i, list.get(i));
            }
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        hssfWorkbook.write(out);
        return out.toByteArray();
    }


    private void setCellValue(HSSFCell cell, CellBean cellBean, CellStyle cellStyle) {
        cell.setCellStyle(cellStyle);
        CreationHelper createHelper = hssfWorkbook.getCreationHelper();
        if (cellBean.getValueByDate() != null) {
            String dateformat = "m/d/yy h:mm";
            if (cellBean.getDateFormat() != null) {
                dateformat = cellBean.getDateFormat();
            }
            cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(dateformat));
            cell.setCellStyle(cellStyle);
            cell.setCellValue(cellBean.getValueByDate());
        } else if (cellBean.getValueByString() != null) {
            cell.setCellValue(createHelper.createRichTextString(cellBean.getValueByString()));
        } else if (cellBean.getValueByDouble() != null) {
            cell.setCellValue(cellBean.getValueByDouble());
        } else if (cellBean.getValueByBoolean() != null) {
            cell.setCellValue(cellBean.getValueByBoolean());
        } else if (cellBean.getValueByInteger() != null) {
            cell.setCellValue(cellBean.getValueByInteger());
        }
    }

    private HSSFFont getContentFont(HSSFWorkbook workbook) {
        // 生成另一个字体
        HSSFFont contentStyle = workbook.createFont();
        contentStyle.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        return contentStyle;
    }

    /**
     * 获取样式
     *
     * @param hssfWorkbook
     * @return
     */
    private CellStyle getCellStyle(HSSFWorkbook hssfWorkbook) {
        HSSFCellStyle contentStyle = hssfWorkbook.createCellStyle();
        contentStyle.setFillForegroundColor(HSSFColor.WHITE.index);
        contentStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        contentStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        contentStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        contentStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        contentStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        contentStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        contentStyle.setWrapText(wrap);
        contentStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        HSSFFont contentFont = getContentFont(hssfWorkbook);
        // 把字体应用到当前的样式
        contentStyle.setFont(contentFont);
        return contentStyle;
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
                        if (cellType == HSSFCell.CELL_TYPE_STRING) {
                            cellBean = new CellBean(cell.getStringCellValue());
                        } else if (cellType == HSSFCell.CELL_TYPE_NUMERIC) {
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
