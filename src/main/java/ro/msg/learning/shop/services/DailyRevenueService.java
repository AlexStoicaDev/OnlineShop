package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Order;
import ro.msg.learning.shop.entities.OrderDetail;
import ro.msg.learning.shop.entities.Revenue;
import ro.msg.learning.shop.repositories.OrderRepository;
import ro.msg.learning.shop.repositories.RevenueRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DailyRevenueService {

    private final OrderRepository orderRepository;
    private final RevenueRepository revenueRepository;

    /**
     * method that creates the revenue for every location and stores the revenues in db
     *
     * @param localDateTime the revenues are created only for this orderDate
     */
    public void createRevenuesForADay(LocalDateTime localDateTime) {

        //returns all the orders created in the given day
        val allOrdersFromADay = orderRepository.findAllByOrderDateAfterAndOrderDateBefore(localDateTime.minusDays(1), localDateTime.plusDays(1));


        //finds all the locations used in that day for orders
        List<Location> allLocationsFromThatDay = new ArrayList<>();
        for (Order order : allOrdersFromADay) {
            for (Location location : order.getLocations()) {

                if (!allLocationsFromThatDay.contains(location)) {

                    allLocationsFromThatDay.add(location);
                    BigDecimal totalRevenue = BigDecimal.ZERO;
                    for (Order order1 : location.getOrders()) {
                        for (OrderDetail orderDetail : order1.getOrderDetails()) {
                            totalRevenue = totalRevenue.add(orderDetail.getProduct().getPrice().multiply(BigDecimal.valueOf(orderDetail.getQuantity())));
                        }
                    }
                    Revenue revenue = new Revenue();
                    revenue.setLocation(location);
                    revenue.setDate(localDateTime);
                    revenue.setSum(totalRevenue);
                    location.getRevenues().add(revenue);
                    revenueRepository.save(revenue);

                }
            }
        }


    }
}
