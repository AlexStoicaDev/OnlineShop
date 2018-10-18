package ro.msg.learning.shop.tasks;

import lombok.RequiredArgsConstructor;
import ro.msg.learning.shop.services.MonthReportService;


@RequiredArgsConstructor
public class MonthReportTask implements Runnable {
    private final MonthReportService monthReportService;

    @Override
    public void run() {

    }
}
