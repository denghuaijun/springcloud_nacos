package com.taikang.business.common.utils.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author : itw_xieyy
 * @date : Created in 2018/6/4
 * @description :
 */
public class ExcelUtils {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    private static final String XLS_END = ".xls";
    private static final String XLSX_END = ".xlsx";
    private static final String XLS_CONTENT_TYPE = "application/vnd.ms-excel";
    private static final String XLSX_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final long FILE_UPLOAD_MAX_SIZE = (long) 1024 * 1024 * 4;

    /**
     * 判断文件是否为空
     *
     * @param file 媒体文件
     * @return true表示文件为空，false表示文件不为空
     */
    public static boolean isEmpty(MultipartFile file) {
        if (file == null || StringUtils.isEmpty(file.getOriginalFilename())
                || file.getSize() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 检查文件大小，最大不能超过4M
     *
     * @param file 多媒体文件
     * @return true表示超过4M，false表示没有超过4M
     */
    public static boolean maxSize(MultipartFile file) {
        return maxSize(file.getSize());
    }

    /**
     * 检查文件大小，最大不能超过4M
     *
     * @param size 文件的大小值
     * @return true表示超过4M，false表示没有超过4M
     */
    public static boolean maxSize(Long size) {
        if (size > FILE_UPLOAD_MAX_SIZE) {
            return true;
        }
        return false;
    }

    /**
     * 获得workbook
     *
     * @param inputStream      输入流
     * @param originalFilename 文件原始名称
     * @return workbook
     * @throws IOException
     */
    public static Workbook getWorkBook(InputStream inputStream, String originalFilename) throws IOException {
        if (originalFilename.endsWith(XLS_END)) {
            return new HSSFWorkbook(inputStream);
        }
        return new XSSFWorkbook(inputStream);
    }

    /**
     * 判断数据列信息是否为空，如果为空，则返回空的信息
     *
     * @param workbook 当前的workbook
     * @param sheetNum 查看第几个sheet
     * @param colNums  总列数
     * @param list     list中包括了非空列的数据值
     * @return
     */
    public static String checkNotBlank(Workbook workbook, Integer sheetNum, Integer colNums, List<Integer> list) {
        Sheet sheet = workbook.getSheetAt(sheetNum);
        if (sheet == null) {
            return "第" + (sheetNum + 1) + "个sheet页为空！";
        }
        //获取总行数
        int rows = sheet.getPhysicalNumberOfRows();
        logger.info("------------------第{}个sheet页【{}】中总共有{}行数据------------------", (sheetNum + 1), sheet.getSheetName(), rows);
        if (rows == 1) {
            return "第" + (sheetNum + 1) + "个sheet页【" + sheet.getSheetName() + "】中没有数据,请检查！";
        }
        for (int i = 0; i < rows; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                return "第" + (sheetNum + 1) + "个sheet页【" + sheet.getSheetName() + "】中第" + (i + 1) + "行信息为空！";
            }
            //遍历列的信息
            for (int j = 0; j < colNums; j++) {
                Cell cell = row.getCell(j);
                //进行空判断，如果获取的cell为空，或者cell中的值为空，或者为空字符串
                if (cell == null & list.contains(j)) {
                    return "第" + (sheetNum + 1) + "个sheet页【" + sheet.getSheetName() + "】中第" + (i + 1) + "行第" + (j + 1) + "列信息不能为空！";
                }
                if (cell != null & list.contains(j)) {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if ((cell.getStringCellValue() == null || StringUtils.isEmpty(cell.getStringCellValue())) & list.contains(j)) {
                        return "第" + (sheetNum + 1) + "个sheet页【" + sheet.getSheetName() + "】中第" + (i + 1) + "行第" + (j + 1) + "列信息不能为空！";
                    }
                }
            }
        }
        return null;
    }

    /**
     * 判断是否为excel文件，通过尾缀和ContentType来进行判断
     *
     * @param file 文件
     * @return
     */
    public static boolean isExcel(MultipartFile file) {
        if (!file.getOriginalFilename().trim().endsWith(XLS_END) & !file.getOriginalFilename().trim().endsWith(XLSX_END)) {
            return false;
        }
        if (!(file.getContentType().equals(XLS_CONTENT_TYPE) || file.getContentType().equals(XLSX_CONTENT_TYPE))) {
            return false;
        }
        return true;
    }

    /**
     * 获取每一页中所有行对应列的内容集合
     * @param sheet
     * @return
     * @author itw_denghj
     */
    public  static List<List<Object>> getCurrentSheetContentList(Sheet sheet, int totalCellNum){
        List<List<Object>> rowList = new ArrayList<>();
        //3、遍历行
        for (int i = 1;i<sheet.getLastRowNum()+1;i++){
            //4、获取每一行对象
            Row row = sheet.getRow(i);
            if (row == null){
                continue;
            }
            List<Object> list = new ArrayList<>();
            //5、遍历列
            for (int j =row.getFirstCellNum();j<totalCellNum;j++){
                //6、获取列对象
                Cell cell = row.getCell(j);
                list.add(ExcelUtils.getCellValue(cell));
            }
            rowList.add(list);
        }
       return rowList;
    }

    /**
     * 对表格中的数值进行格式化输出，获取每一个单元格中的内容
     * @param cell
     * @return value
     * @author itw_denghj
     */
    public  static String getCellValue(Cell cell){
        String value ="";
        if (cell ==null){
            return value;
        }
        switch (cell.getCellType()){
            //数值型
            case Cell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)){//如果是date类型，则获取该cell的date值
                    Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    value = dateFormat.format(date);
                }else{//若为纯数字
                    BigDecimal decimal = new BigDecimal(cell.getNumericCellValue());
                    value = decimal.toString();
                    //解决1234.0 去掉后面的.0
                    if (StringUtils.isNotEmpty(value.trim())){
                        String[] item = value.split(".");
                        if (item.length>1 && "0".equals(item[1].trim())){
                            value=item[0].trim();
                        }
                    }
                }
                break;
                //字符串类型
            case Cell.CELL_TYPE_STRING:
                value = cell.getStringCellValue().toString();
                break;
                //若为公式型
            case Cell.CELL_TYPE_FORMULA:
                //读取公式的值
                value = String.valueOf(cell.getNumericCellValue());
                if (value.equals("NaN")){//若值为不是数字型或者非法的，则直接获取字符串
                    value = cell.getStringCellValue().toString();
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = " "+cell.getBooleanCellValue();
                break;
            default:
                value = cell.getStringCellValue().toString();
        }
        if ("null".equals(value)){
            value="";
        }
        return value;
    }

    /**
     * 设置头信息，包括合并单元格的信息
     *
     * @param workbook
     * @param sheet
     * @param map
     * @param headData
     */
    public static void setHeadStyle(Workbook workbook, Sheet sheet, Map map, Map headData) {
        CellStyle headStyle = workbook.createCellStyle();
        //设置边框
//        headStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
//        headStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        headStyle.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
        headStyle.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);

        //设置背景色以及完全填充
        headStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        headStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        //居中显示
        headStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        Font font = workbook.createFont();
        font.setFontName("微软雅黑");
        //设置字体大小
        font.setFontHeightInPoints((short) 10);
        //粗体显示
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headStyle.setFont(font);

        CellRangeAddress cellRangeAddress;
        Row row = sheet.createRow(0);
        //设置行高400
        row.setHeight((short) 400);
        Set keySet = map.keySet();
        for (Object o : keySet) {
            Integer key = (Integer) o;
            cellRangeAddress = new CellRangeAddress(0, 0, key, (Integer) map.get(key));
            sheet.addMergedRegion(cellRangeAddress);
            Cell cell = row.createCell(key);
            cell.setCellValue(headData.get(key).toString());
            cell.setCellStyle(headStyle);
        }
    }

    public static void setTitleData(Workbook workbook, Sheet sheet, String[] data) {
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        Row row = sheet.createRow(1);
        row.setHeight((short) 400);
        for (Integer i = 0; i < data.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(data[i]);
            cell.setCellStyle(titleStyle);
        }
    }

}