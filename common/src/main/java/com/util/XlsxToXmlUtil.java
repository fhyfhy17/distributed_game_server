package com.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class XlsxToXmlUtil {
    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void write(String path) throws IOException {
        List<String> filelist = FileUtil.getFiles(path, ".xlsx");
        for (String fileName : filelist) {
            // 这里是生成工作簿
            Workbook wb = null;
            InputStream instream = new FileInputStream(fileName);
            try {
                wb = new XSSFWorkbook(instream);
                Iterator<Sheet> it = wb.sheetIterator();
                // 获取第一张Sheet表
                while (it.hasNext()) {
                    Sheet sheet = it.next();
                    if (!isLetterDigit(sheet.getSheetName())) {
                        continue;
                    }
                    System.out.println("正在生成" + fileName + "_" + sheet.getSheetName());


                    Row row = sheet.getRow(0);
                    if (row == null) {
                        return;
                    }
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < row.getLastCellNum(); i++) {
                        Cell cell = row.getCell(i);
                        if (cell != null) {
                            list.add(cell.getRichStringCellValue().getString());
                        } else {
                            list.add(null);
                        }

                    }

                    // 创建根节点;
                    Element root = new Element("root");
                    // 将根节点添加到文档中；
                    Document Doc = new Document(root);
                    for (int i = 3; i <= sheet.getLastRowNum(); i++) {
                        Element elements = new Element("tr");
                        Row dataRow = sheet.getRow(i);
                        String s = getCellValue(dataRow.getCell(0));
                        if (s == null || "".equals(s)) {
                            continue;
                        }

                        for (int j = 0; j < list.size(); j++) {
                            if (list.get(j) == null || "".equals(list.get(j))) {
                                continue;
                            }
                            String name = list.get(j);
                            Cell cell = dataRow.getCell(j);
                            if (cell == null) {
                                elements.setAttribute(name, "");
                                root.addContent(elements.detach());
                                continue;
                            }
                            elements.setAttribute(name, getCellValue(cell).trim());
                            root.addContent(elements.detach());
                        }

                    }
                    Format format = Format.getPrettyFormat();
                    XMLOutputter XMLOut = new XMLOutputter(format);
                    int index = fileName.lastIndexOf("\\");
                    XMLOut.output(Doc, new FileOutputStream(fileName.substring(0, index) + "\\xml\\" + fileName.substring(index, fileName.length()) + "_" + sheet.getSheetName() + ".xml"));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String getCellValue(Cell cell) {
        switch (cell.getCellTypeEnum()) {
            case BLANK:
                break;
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case ERROR:
                return String.valueOf(cell.getErrorCellValue());
            case FORMULA:
                String.valueOf(cell.getCellFormula());
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {

                    return String.valueOf(format.format(cell.getDateCellValue()));
                } else {
                    String s = String.valueOf(cell.getNumericCellValue());

                    if (s.endsWith(".0"))
                        s = s.substring(0, s.indexOf(".0"));
                    return s;
                }
            case STRING:

                return cell.getRichStringCellValue().getString();

            default:
                return cell.getStringCellValue();
        }
        return null;
    }

    public static boolean isLetterDigit(String str) {
        String regex = "^[a-z0-9A-Z_-]+$";// 加入横线，下划线
        // \u4e00-\u9fa5 这是汉字
        return str.matches(regex);
    }

    public static void main(String[] args) throws IOException {
        write("D://");
        // read();
        // getFiles(".");
    }
}