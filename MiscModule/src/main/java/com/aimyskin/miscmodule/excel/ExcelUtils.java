package com.aimyskin.miscmodule.excel;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.aimyskin.miscmodule.utils.TimeUtil;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bolts.Continuation;
import bolts.Task;

public class ExcelUtils {

    private static final String TAG = "ExcelUtils";

    /**
     * 判断file是否是Excel
     *
     * @param file
     * @return
     */
    public static boolean checkExcelFile(File file) {
        if (file == null) {
            return false;
        }
        String name = file.getName();
        //”.“ 需要转义字符
        String[] strings = name.split("\\.");
        //划分后的小于2个元素说明不可获取类型名
        if (strings.length < 2) {
            return false;
        }
        String typeName = strings[strings.length - 1];
        //满足xls或者xlsx才可以
        return "xls".equals(typeName) || "xlsx".equals(typeName);
    }

    private static boolean isEmptyRow(Row row) {
        boolean isEmpty = true;
        if (row == null) {
            return isEmpty;
        }
        for (int c = 0; c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null) {
                isEmpty = false;
                return isEmpty;
            }
        }
        return isEmpty;

    }

    /**
     * 处理合并单元格
     */
    public static String getMergedCellValue(XSSFSheet sheet, int rowIndex, int columnIndex, FormulaEvaluator formulaEvaluator) {
        XSSFRow xssfRow = sheet.getRow(rowIndex);
        if (xssfRow == null) {
            return "";
        }
        Cell cell = xssfRow.getCell(columnIndex);
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress mergedRegion = sheet.getMergedRegion(i);
            if (mergedRegion.isInRange(rowIndex, columnIndex)) {
                Cell mergedCell = sheet.getRow(mergedRegion.getFirstRow()).getCell(mergedRegion.getFirstColumn());
                return getCellAsString(mergedCell, formulaEvaluator);
            }
        }
        return getCellAsString(cell, formulaEvaluator);
    }

    /**
     * 读取excel文件中每一行的内容
     *
     * @param formulaEvaluator
     * @return
     */
    private static String getCellAsString(Cell cell, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = "" + cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = "" + numericValue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = "" + cellValue.getStringValue();
                    break;
                default:
                    break;
            }
        } catch (NullPointerException e) {
        }
        return value.trim();
    }

    /**
     * @param file         读取Excel文件
     * @param sheetAtIndex 读取Excel的sheet
     * @param startRows    开始的行号
     * @param startCells   开始的列号
     * @param listener     返回读取到的数据
     */
    public static void readExcel(File file, int sheetAtIndex, int startRows, int startCells, ReadExcelListener listener) {
        if (file == null || !checkExcelFile(file)) {
            Log.e("readExcel", "读取Excel出错，文件为空文件");
            return;
        }
        try {
            InputStream stream = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(stream);
            XSSFSheet sheet = workbook.getSheetAt(sheetAtIndex);
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            int rowsCount = sheet.getPhysicalNumberOfRows();// 有效行数
            int firstRowIndex = Math.max(sheet.getFirstRowNum(), startRows - 1); // Index
            int lastRowIndex = sheet.getLastRowNum(); //Index
            Log.d(TAG, "readExcel: " +firstRowIndex + "    " + lastRowIndex );
            for (int i = firstRowIndex; i <= lastRowIndex; i++) {
                Row row = sheet.getRow(i);
                if (isEmptyRow(row)) {
                    Log.d(TAG, "readExcel: 空行" );
                    continue;
                }
                if (listener != null) {
                    listener.readRowStart();
                }
                int cellsCount = row.getPhysicalNumberOfCells();
                int firstCellsNum = row.getFirstCellNum(); // Index
                int lastCellsNum = row.getLastCellNum(); //num

                for (int j = Math.max(startCells - 1, 0); j < lastCellsNum; j++) {
                    String value = getMergedCellValue(sheet, i, j, formulaEvaluator);
                    if (listener != null) {
                        listener.readCellsExcel(i, j, value);
                    }
                }
                if (listener != null) {
                    listener.readRowEnd();
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * @param context
     * @param sheetName
     * @param startRows   开始行号 第一行为标题
     * @param startCells  开始列号
     * @param titleMap    Integer：行号顺序，从0开始到map.size结束。String：titleName。决定了有多少列的数据
     * @param exportExcel 表数据 map中的Integer对应titleMap中的Integer，代表了列index
     * @param workbook
     * @param fileDest
     * @param columnBold  需要加粗红色字体显示的 列的index集合
     */
    public static void writeExcel(Context context, String sheetName, int startRows, int startCells, Map<Integer, String> titleMap,
                                  List<Map<Integer, String>> exportExcel, XSSFWorkbook workbook, File fileDest, List<Integer> columnBold) {
        try {
            if (workbook == null) {
                return;
            }
            if (columnBold == null) {
                columnBold = new ArrayList<>();
            }

            XSSFSheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName(sheetName));
            int startRowsIndex = Math.max(0, startRows - 1); // 开始行 index
            int startCellsIndex = Math.max(0, startCells - 1); // 开始列 index

            CellStyle style = workbook.createCellStyle();
            // 创建一个字体对象
            Font font = workbook.createFont();
            font.setBold(true); // 设置字体加粗
            font.setColor(IndexedColors.RED.getIndex()); // 设置字体颜色为红色
            // 将字体应用到样式
            style.setFont(font);
            // 写入title
            // 创建title行
            Row rowTitle = sheet.createRow(startRowsIndex);
            for (Map.Entry<Integer, String> entry : titleMap.entrySet()) {
                // 创建单元格
                int key = entry.getKey();
                String value = entry.getValue();
                Cell cell = rowTitle.createCell(key + startCellsIndex);
                cell.setCellStyle(style);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue(value);
            }

            // 写入数据
            for (int i = 0; i < exportExcel.size(); i++) {
                Row row = sheet.createRow(startRowsIndex + 1 + i); // 加1为错开title行
                Map<Integer, String> map = exportExcel.get(i);

                for (Map.Entry<Integer, String> entry : map.entrySet()) {
                    int key = entry.getKey();
                    String value = entry.getValue();
                    Cell cell = row.createCell(key + startCellsIndex);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if (columnBold.contains(key)) {
                        cell.setCellStyle(style);
                    }
                    cell.setCellValue(value);
                }
            }
            if (!fileDest.exists()) {
                fileDest.createNewFile();
            }
            OutputStream outputStream = context.getContentResolver().openOutputStream(Uri.fromFile(fileDest));
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 读取 Assets文件 Language.xml 转excel文件并保存到本地
     *
     * @param context
     */
    public static void readAssetsLanguage(Context context) {
        String app_path = Environment.getExternalStorageDirectory().getPath();
        String name = "Language_" + TimeUtil.getCurDate("MMdd_HHmmss_SSS") + ".xlsx";

        Task task = Task.forResult(null);
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                Map<String, Object> paramsMap = new HashMap<>();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                InputStream inputStream = context.getAssets().open("Language.xml");
                parser.setInput(inputStream, "utf-8");
                int eventType = parser.getEventType();
                String name = "", content = "";
                String labeType = "";
                List<String> list = new ArrayList<>();
                StringBuilder stringBuilder = new StringBuilder();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            labeType = parser.getName();
                            if ("string".equals(labeType)) {
                                stringBuilder = new StringBuilder();
                                name = parser.getAttributeValue(null, "name");
                            }
                            if ("string-array".equals(labeType)) {
                                name = parser.getAttributeValue(null, "name");
                                list = new ArrayList<>();
                            }
                            if (!"string".equals(labeType) && !"string-array".equals(labeType)) {
                                stringBuilder.append("<");
                                stringBuilder.append(labeType);
                                stringBuilder.append(">");
                            }
                            break;
                        case XmlPullParser.TEXT:
                            if (!TextUtils.isEmpty(name)) {
                                content = parser.getText();
                                stringBuilder.append(content);
                                content = "";
                            }
                            if (!TextUtils.isEmpty(name) && "item".equals(labeType)) {
                                content = parser.getText();
                                if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(content.trim())) {
                                    list.add(content);
                                }
                                content = "";
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            labeType = parser.getName();
                            if (!"string".equals(labeType) && !"string-array".equals(labeType)) {
                                stringBuilder.append("</");
                                stringBuilder.append(labeType);
                                stringBuilder.append(">");
                            }
                            if ("string".equals(labeType)) {
                                paramsMap.put(name, stringBuilder.toString());
                                name = "";
                            }
                            if ("string-array".equals(labeType)) {
                                paramsMap.put(name, list);
                                name = "";
                            }
                            break;
                    }
                    eventType = parser.next();
                }
                inputStream.close();
                return Task.forResult(paramsMap);
            }
        }, Task.BACKGROUND_EXECUTOR);
        task = task.onSuccessTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                Map<String, Object> paramsMap = (Map<String, Object>) task.getResult();
                if (paramsMap == null || paramsMap.size() == 0) {
                    return Task.forError(new Exception("Map无数据"));
                }
                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFSheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName("Language"));

                Set<String> keySet = paramsMap.keySet();
                Iterator<String> iterator = keySet.iterator();

                int startRow = 0;
                int startCell = 0;
                while (iterator.hasNext()) {
                    Row row = sheet.createRow(startRow);// 创建新行

                    int cellIndex = startCell;
                    String key = iterator.next();
                    if (!TextUtils.isEmpty(key)) {
                        Cell cell_key = row.createCell(cellIndex);
                        cell_key.setCellValue(key.trim());
                    }

                    Object value = paramsMap.get(key);
                    cellIndex++;
                    if (value instanceof String) {
                        String string = (String) value;
                        Cell cell = row.createCell(cellIndex);
                        cell.setCellValue(string.trim());
                    } else if (value instanceof String[]) {
                        String[] string = (String[]) value;
                        for (String s : string) {
                            startRow++;
                            Row rowV = sheet.createRow(startRow);// 创建新行
                            Cell cell = rowV.createCell(cellIndex);
                            cell.setCellValue(s.trim());
                        }
                    } else if (value instanceof List) {
                        List<String> string = (List<String>) value;
                        for (String s : string) {
                            startRow++;
                            Row rowV = sheet.createRow(startRow);// 创建新行
                            Cell cell = rowV.createCell(cellIndex);
                            cell.setCellValue(s.trim());
                        }
                    }
                    startRow++;
                }

                File file = new File(app_path, name);
                OutputStream outputStream = context.getContentResolver().openOutputStream(Uri.fromFile(file));
                workbook.write(outputStream);
                outputStream.flush();
                outputStream.close();
                return task;
            }
        }, Task.BACKGROUND_EXECUTOR);

    }

    /**
     * 读取 本地excel文件 并保存到本地 Language.xml
     * 读取excel转map数据再写入Language.xml
     */
    public static void readExcelToLanguageXml(Context context) {
        String app_path = Environment.getExternalStorageDirectory().getPath();
        String name = "Language_" + TimeUtil.getCurDate("MMdd_HHmmss_SSS") + ".xml";
        String xlsx = "Language.xlsx";
        Task task = Task.forResult(null);
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                HashMap<String, Object> map = new HashMap<>();
                File fileSrc = new File(app_path, xlsx);

                InputStream stream = new FileInputStream(fileSrc);

                XSSFWorkbook workbook = new XSSFWorkbook(stream);
                XSSFSheet sheet = workbook.getSheetAt(0);
                FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
                int rowsCount = sheet.getPhysicalNumberOfRows();// 有效行数

                int firstRowNum = sheet.getFirstRowNum(); // Index
                int lastRowNum = sheet.getLastRowNum(); //Index
                Log.d(TAG, "数据then: " + firstRowNum + "  lastRowNum = " + lastRowNum + "  rowsCount = " + rowsCount);

                String keyArray = "";

                for (int i = firstRowNum; i <= lastRowNum; i++) {
                    Row row = sheet.getRow(i);
                    if (row == null || row.getPhysicalNumberOfCells() <= 0) {
                        Log.d(TAG, "then: 无数据" + i);
                    }
                    int cellsCount = row.getPhysicalNumberOfCells();
                    int firstCellsNum = row.getFirstCellNum(); // Index
                    int lastCellsNum = row.getLastCellNum(); //Index
                    Log.d(TAG, "数据then cellsCount: " + cellsCount + " firstCellsNum: " + firstCellsNum + " lastCellsNum: " + lastCellsNum);

                    String key = getMergedCellValue(sheet, i, 0, formulaEvaluator);
                    String value = getMergedCellValue(sheet, i, 1, formulaEvaluator);
                    Log.d(TAG, "then: " + key + " value " + value);
                    if (TextUtils.isEmpty(key) && TextUtils.isEmpty(value)) {
                        continue;
                    }
                    if (!TextUtils.isEmpty(key)) {
                        String key_1 = getMergedCellValue(sheet, i + 1, 0, formulaEvaluator);
                        if (TextUtils.isEmpty(key_1) && TextUtils.isEmpty(value)) {
                            keyArray = key;
                            map.put(key, new String[]{});
                        } else {
                            keyArray = "";
                            map.put(key, value);
                        }
                    } else {
                        if (!TextUtils.isEmpty(value) && !TextUtils.isEmpty(keyArray)) {
                            String[] strings = (String[]) map.get(keyArray);
                            String[] newArray = Arrays.copyOf(strings, strings.length + 1);
                            newArray[newArray.length - 1] = value;
                            map.put(keyArray, newArray);
                        } else {
                            keyArray = "";
                        }
                    }
                }
                Log.d(TAG, "then: map size" + map.size());
                return Task.forResult(map);
            }
        }, Task.BACKGROUND_EXECUTOR);
        task = task.onSuccessTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                Log.d(TAG, "then: map");
                HashMap<String, Object> map = (HashMap<String, Object>) task.getResult();
                File file = new File(app_path, name);

                FileWriter fileWriter = new FileWriter(file);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                bufferedWriter.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                bufferedWriter.newLine();  // 换行
                bufferedWriter.write("<resources>");
                bufferedWriter.newLine();  // 换行

                Set<String> keySet = map.keySet();
                Iterator<String> iterator = keySet.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    Object value = map.get(key);
                    StringBuilder stringBuilder = new StringBuilder();
                    if (value instanceof String) {
                        String line = "    <string name=\"" + key + "\">" + ((String) value) + "</string>";
                        bufferedWriter.write(line);
                    } else if (value instanceof String[]) {
                        String[] strings = (String[]) value;
                        stringBuilder.delete(0, stringBuilder.length());
                        stringBuilder.append("    <string-array name=\"" + key + "\"> \n");
                        for (String string : strings) {
                            stringBuilder.append("      <item>" + string + "</item> \n");
                        }
                        stringBuilder.append("    </string-array>");
                        bufferedWriter.write(stringBuilder.toString());

                    } else if (value instanceof List) {
                        List<String> list = (List<String>) value;
                        stringBuilder.delete(0, stringBuilder.length());
                        stringBuilder.append("    <string-array name=\"" + key + "\"> \n");
                        for (Object string : list) {
                            stringBuilder.append("      <item>" + string + "</item> \n");
                        }
                        stringBuilder.append("    </string-array>");
                        bufferedWriter.write(stringBuilder.toString());
                    }
                    bufferedWriter.newLine();  // 换行
                }
                bufferedWriter.write("</resources>");

                // 关闭流
                bufferedWriter.close();
                fileWriter.close();

                return task;
            }
        }, Task.BACKGROUND_EXECUTOR);
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                Toast.makeText(context, "执行完了" ,Toast.LENGTH_LONG).show();
                return task;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }
}
