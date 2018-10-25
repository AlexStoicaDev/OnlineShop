package ro.msg.learning.shop.tasks;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import ro.msg.learning.shop.services.DailyRevenueService;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class DailyTask {

    private final DailyRevenueService dailyRevenueService;

    //Fires at 12 PM every day, calculates the revenues for all the locations, on the previous day
    @Scheduled(cron = "0 0 12 * * ?")
    public void createRevenueForAllLocations() {
        dailyRevenueService.createRevenuesForADay(LocalDateTime.now().minusDays(1));
    }
}
