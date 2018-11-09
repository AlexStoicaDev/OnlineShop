package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.repositories.ReportRepository;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportController {

    private final ReportRepository reportRepository;


    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{year}/{month}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] getReport(@PathVariable Integer year, @PathVariable Integer month) {

        return reportRepository.findByMonthAndYear(month, year).getFile();

    }
}
