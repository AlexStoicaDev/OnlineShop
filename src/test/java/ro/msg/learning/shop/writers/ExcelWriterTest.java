package ro.msg.learning.shop.writers;

import lombok.val;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.wrappers.DateProductIdQuantityTotalRevenueWrapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExcelWriterTest {

    private DateProductIdQuantityTotalRevenueWrapper dateProductIdQuantityTotalRevenueWrapper;


    @Before
    public void setUp() {

        dateProductIdQuantityTotalRevenueWrapper = new DateProductIdQuantityTotalRevenueWrapper();
        dateProductIdQuantityTotalRevenueWrapper.setProductId(1);
        dateProductIdQuantityTotalRevenueWrapper.setTotalRevenue(25);
        dateProductIdQuantityTotalRevenueWrapper.setQuantity(5);
        dateProductIdQuantityTotalRevenueWrapper.setOrderDate(LocalDateTime.now());
    }

    @Test
    public void writeExcelTest() {
        val bytes = new ExcelWriter().writeExcel(Collections.singletonList(dateProductIdQuantityTotalRevenueWrapper)).toByteArray();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        XSSFWorkbook wb = null;
        try {
            wb = new XSSFWorkbook(byteArrayInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        XSSFSheet sheet = wb.getSheetAt(0);

        XSSFRow row;
        XSSFCell cell;
        Iterator rows = sheet.rowIterator();

        List<String> values = new ArrayList<>();

        rows.next();
        row = (XSSFRow) rows.next();
        Iterator cells = row.cellIterator();
        while (cells.hasNext()) {
            cell = (XSSFCell) cells.next();
            values.add(cell.getStringCellValue());
        }
        val orderDate = dateProductIdQuantityTotalRevenueWrapper.getOrderDate().toString().split("T");

        assertEquals("Order Date:", orderDate[0] + " " + orderDate[1], values.get(0));
        assertEquals("Product Id:", dateProductIdQuantityTotalRevenueWrapper.getProductId().toString(), values.get(1));
        assertEquals("Quantity:", dateProductIdQuantityTotalRevenueWrapper.getQuantity().toString(), values.get((2)));
        assertEquals("Total Revenue", dateProductIdQuantityTotalRevenueWrapper.getTotalRevenue().toString(), values.get(3));


    }
}
