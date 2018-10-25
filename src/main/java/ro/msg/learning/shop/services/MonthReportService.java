package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.repositories.OrderRepository;
import ro.msg.learning.shop.wrappers.DateProductIdQuantityTotalRevenueWrapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MonthReportService {
    private final OrderRepository orderRepository;

    public List<DateProductIdQuantityTotalRevenueWrapper> getMonthReportInfo(LocalDateTime localDateTime1, LocalDateTime localDateTime2) {

        List<DateProductIdQuantityTotalRevenueWrapper> dateProductIdQuantityTotalRevenueWrappers = new ArrayList<>();


        val allOrders = orderRepository.findAllByOrderDateAfterAndOrderDateBefore(localDateTime1, localDateTime2);
        allOrders.forEach(order -> order.getOrderDetails().forEach(orderDetail -> {

            DateProductIdQuantityTotalRevenueWrapper dateProductIdQuantityTotalRevenueWrapper = new DateProductIdQuantityTotalRevenueWrapper();
            dateProductIdQuantityTotalRevenueWrapper.setProductId(orderDetail.getProduct().getId());
            dateProductIdQuantityTotalRevenueWrapper.setLocalDateTime(order.getOrderDate());
            val quantity = orderDetail.getQuantity();
            val revenue = quantity * orderDetail.getProduct().getPrice().intValue();


            if (dateProductIdQuantityTotalRevenueWrappers.contains(dateProductIdQuantityTotalRevenueWrapper)) {

                val i = dateProductIdQuantityTotalRevenueWrappers.indexOf(dateProductIdQuantityTotalRevenueWrapper);
                val dateProductIdQuantityTotalRevenueWrapper1 = dateProductIdQuantityTotalRevenueWrappers.get(i);
                dateProductIdQuantityTotalRevenueWrapper1.setQuantity(dateProductIdQuantityTotalRevenueWrapper1.getQuantity() + quantity);
                dateProductIdQuantityTotalRevenueWrapper1.setTotalRevenue(dateProductIdQuantityTotalRevenueWrapper1.getTotalRevenue() + revenue);
            } else {
                dateProductIdQuantityTotalRevenueWrapper.setQuantity(quantity);
                dateProductIdQuantityTotalRevenueWrapper.setTotalRevenue(revenue);
                dateProductIdQuantityTotalRevenueWrappers.add(dateProductIdQuantityTotalRevenueWrapper);
            }
        }));

        return dateProductIdQuantityTotalRevenueWrappers;
    }

}
