package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.entities.Report;
import ro.msg.learning.shop.repositories.ReportRepository;

@RequestMapping("/api/report")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    private final ReportRepository reportRepository;

    @GetMapping(path = "/{year}/{month}")
    public Report getReport(@PathVariable Integer year, @PathVariable Integer month) {
        return reportRepository.findByMonthAndYear(month, year);

    }
}
