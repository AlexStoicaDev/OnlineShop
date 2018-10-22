package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.repositories.ReportRepository;
import ro.msg.learning.shop.tasks.DailyTask;


@RequestMapping("/api/report")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    private final ReportRepository reportRepository;
    private final DailyTask dailyTask;

    @GetMapping(path = "/{year}/{month}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] getReport(@PathVariable Integer year, @PathVariable Integer month) {
        return reportRepository.findByMonthAndYear(month, year).getFile();

    }
}
