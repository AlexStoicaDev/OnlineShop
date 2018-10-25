package ro.msg.learning.shop.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import ro.msg.learning.shop.repositories.ReportRepository;
import ro.msg.learning.shop.services.DailyRevenueService;
import ro.msg.learning.shop.services.MonthReportService;
import ro.msg.learning.shop.tasks.DailyTask;
import ro.msg.learning.shop.tasks.MonthTask;
import ro.msg.learning.shop.writers.ExcelWriter;

@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class SchedulingConfiguration {

    private final DailyRevenueService dailyRevenueService;
    private final ExcelWriter excelWriter;
    private final MonthReportService monthReportService;
    private final ReportRepository reportRepository;

    @Bean
    public DailyTask dailyTask() {
        return new DailyTask(dailyRevenueService);
    }

    @Bean
    public MonthTask monthTask() {
        return new MonthTask(excelWriter, monthReportService, reportRepository);
    }
}
