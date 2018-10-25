package ro.msg.learning.shop.tasks;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.scheduling.annotation.Scheduled;
import ro.msg.learning.shop.entities.Report;
import ro.msg.learning.shop.repositories.ReportRepository;
import ro.msg.learning.shop.services.MonthReportService;
import ro.msg.learning.shop.writers.ExcelWriter;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;


@RequiredArgsConstructor
public class MonthTask {

    private final ExcelWriter excelWriter;
    private final MonthReportService monthReportService;
    private final ReportRepository reportRepository;

    //Fires on the  1st day every month at 12 PM,  creates a report of all the purchased products from the previous month
    @Scheduled(cron = "0 0 0 1 1/1 *")
    public void createExcel() {
        val now = LocalDateTime.now();

        Report report = new Report();
        val monthReportInfo = monthReportService.getMonthReportInfo(now.minusMonths(1).minusDays(1), now);
        ByteArrayOutputStream outputStream = excelWriter.writeExcel(monthReportInfo);
        report.setMonth(now.minusMonths(1).getMonth().getValue());
        report.setYear(now.getYear());
        report.setFile(outputStream.toByteArray());
        report.setDateProductIdQuantityTotalRevenueWrappers(monthReportInfo);

        reportRepository.save(report);
    }
}
