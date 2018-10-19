package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.entities.Revenue;
import ro.msg.learning.shop.repositories.LocationRepository;
import ro.msg.learning.shop.repositories.OrderRepository;
import ro.msg.learning.shop.repositories.RevenueRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyRevenueService {
    private final OrderRepository orderRepository;
    private final RevenueRepository revenueRepository;
    private final LocationRepository locationRepository;


    public void createRevenuesForADay(LocalDateTime localDateTime) {

        final val allOrdersFromADay = orderRepository.findAllByOrderDateAfterAndOrderDateBefore(localDateTime.minusDays(1), localDateTime.plusDays(1));
        List<Revenue> revenues = new ArrayList<>();

        List<Location> allLocationsFromThatDay = new ArrayList<>();
        allOrdersFromADay.forEach(order -> order.getLocations().forEach(location -> {
            if (!allLocationsFromThatDay.contains(location)) {
                allLocationsFromThatDay.add(location);
            }
        }));

        for (Location location : allLocationsFromThatDay) {

            BigDecimal totalRevenue = BigDecimal.ZERO;
            for (Order order : location.getOrders()) {
                for (OrderDetail orderDetail : order.getOrderDetails()) {
                    totalRevenue = totalRevenue.add(orderDetail.getProduct().getPrice().multiply(BigDecimal.valueOf(orderDetail.getQuantity())));
                }
            }
            Revenue revenue = new Revenue();
            revenue.setLocation(location);
            revenue.setDate(localDateTime);
            revenue.setSum(totalRevenue);
            location.getRevenues().add(revenue);
            locationRepository.save(location);
            revenueRepository.save(revenue);


            revenues.add(revenue);
        }

    }
}
