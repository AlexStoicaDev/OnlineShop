package ro.msg.learning.shop.tasks;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.scheduling.annotation.Scheduled;
import ro.msg.learning.shop.services.MonthReportService;
import ro.msg.learning.shop.writers.ExcelWriter;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class MonthTask {

    private final ExcelWriter excelWriter;
    private final MonthReportService monthReportService;

    //Fires on the  1st day every month at 12 PM,  creates a report of all the purchased products from the previous month
    @Scheduled(cron = "0 0 0 1 1/1 *")
    public void createExcelAndStoreItInMongoDb() {
        final val now = LocalDateTime.now();


        final val productQuantityTotalRevenueForEachProductSoldMappedByDate = monthReportService.
            getProductQuantityTotalRevenueForEachProductSoldMappedByDate(now.minusMonths(1).minusDays(1), now);
        excelWriter.writeExcel(productQuantityTotalRevenueForEachProductSoldMappedByDate);

        //store in mongo db
    }
}
