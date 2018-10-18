package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Revenue;
import ro.msg.learning.shop.repositories.RevenueRepository;

@Service
@RequiredArgsConstructor
public class DailyRevenueService {
    private final RevenueRepository revenueRepository;


    public void calculateDailyRevenue(Location location) {
        Revenue revenue = new Revenue();

    }
}
