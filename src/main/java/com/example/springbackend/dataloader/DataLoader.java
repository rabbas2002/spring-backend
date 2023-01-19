package com.example.springbackend.dataloader;

import com.example.springbackend.model.Product;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.*;
public class DataLoader {
    public static String TYPE =  "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[]HEADERs = {"Id","code","name","batch","stock","deal","free","mrp","rate","exp","company","supplier"};
    static String SHEET = "sample_inventory";
    public static boolean hasExcelFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }
    private static Date getDate(String dateString){
        HashMap<String,Integer>months = new HashMap<>();
        months.put("Jan",Calendar.JANUARY);
        months.put("Feb",Calendar.FEBRUARY);
        months.put("Mar",Calendar.MARCH);
        months.put("Apr",Calendar.APRIL);
        months.put("May",Calendar.MAY);
        months.put("Jun",Calendar.JUNE);
        months.put("Jul",Calendar.JULY);
        months.put("Aug",Calendar.AUGUST);
        months.put("Sept",Calendar.SEPTEMBER);
        months.put("Oct",Calendar.OCTOBER);
        months.put("Nov",Calendar.NOVEMBER);
        months.put("Dec",Calendar.DECEMBER);
        if(dateString.length()>10) {
            int date = Integer.parseInt(dateString.substring(0,2));
            String month = "";
            int i = 3;
            while (dateString.charAt(i)!='-'){
                month+=dateString.charAt(i);
                i++;
            }
            i++;
            int year  = Integer.parseInt(dateString.substring(i));
            return new Date(year-1900,months.get(month),date);
        }
        else{
            return null;
        }
    }
    private static Product setProductData(Row currentRow){
        Product product = new Product();
        //setID
        product.setId((long)currentRow.getRowNum());
        product.setCode(currentRow.getCell(0).toString());
        product.setName(currentRow.getCell(1).toString());
        if(currentRow.getCell(2)!=null){
            product.setBatch(currentRow.getCell(2).toString());
        }
        else{
            product.setBatch(" ");
        }
        product.setStock(Math.round(Float.parseFloat(currentRow.getCell(3).toString())));
        product.setDeal(Math.round((Float.parseFloat(currentRow.getCell(4).toString()))));
        product.setFree(Math.round(Float.parseFloat(currentRow.getCell(5).toString())));
        product.setMrp(Float.parseFloat(currentRow.getCell(6).toString()));
        product.setRate(Float.parseFloat(currentRow.getCell(7).toString()));
        product.setCompany(currentRow.getCell(9).toString());
        if(currentRow.getCell(10)==null){
            product.setSupplier("");
        }
        else{
            product.setSupplier(currentRow.getCell(10).toString());
        }
        String dateString = currentRow.getCell(8).toString();
        Date expDate = getDate(dateString);
        product.setExp(expDate);
        return product;
    }
    public static List<Product> excelToProducts(InputStream is) {
        Logger logger = LoggerFactory.getLogger(DataLoader.class);
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<Product> productList = new ArrayList<>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                productList.add(setProductData(currentRow));

            }
            workbook.close();
            logger.info("Parsed the Excel File Successfully!");
            return productList;
        } catch (IOException e) {
            logger.error("failed to parse Excel file: " + e.getMessage());
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }


}
