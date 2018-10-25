package ro.msg.learning.shop.writers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.wrappers.DateProductIdQuantityTotalRevenueWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Class that creates a report of all the purchased products from that month in a XLSX format
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class ExcelWriter {


    public ByteArrayOutputStream writeExcel(List<DateProductIdQuantityTotalRevenueWrapper> monthReportInfo) {


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();


        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            XSSFSheet sheet = workbook.createSheet("Month report day by day");

            createTableHeader(sheet, "Date", "Product Id", "Quantity", "Total RevenueRepository");

            int rowNum = 1;
            for (DateProductIdQuantityTotalRevenueWrapper info : monthReportInfo) {

                ArrayList<Object> values = new ArrayList<>();
                val s = info.getLocalDateTime().toString().split("T");

                String dayOfMonth = s[0] + " " + s[1];
                values.add(dayOfMonth);
                values.add(info.getProductId().toString());
                values.add(info.getQuantity().toString());
                values.add(info.getTotalRevenue().toString());
                createTableRow(sheet, rowNum++, values.toArray());

            }
            workbook.write(byteArrayOutputStream);
        } catch (
            IOException e)

        {
            log.error("error creating excel for this month{}", e);
        }
        return byteArrayOutputStream;
    }

    private void createTableRow(XSSFSheet sheet, int rowNumber, Object... values) {
        Row row = sheet.createRow(rowNumber);
        int colNum = 0;
        Cell cell;
        for (Object value : values) {
            cell = row.createCell(colNum++);
            cell.setCellValue(String.valueOf(value));
        }

    }

    private void createTableHeader(XSSFSheet sheet, String... headers) {
        Row row = sheet.createRow(0);
        int colNum = 0;
        Cell cell;
        for (String header : headers) {
            cell = row.createCell(colNum++);
            cell.setCellValue(header);
        }

    }
}
