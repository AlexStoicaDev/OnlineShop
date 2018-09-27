package ro.msg.learning.shop.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.dtos.OrderDetailDto;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.entities.embeddables.Address;
import ro.msg.learning.shop.strategies.LocationStrategy;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LocationServiceTest {
    @Mock
    private LocationStrategy locationStrategy;

    private Location location;

    @Before
    public void setUp() {
        location = new Location();
        location.setId(1);
        location.setName("test name");
        location.setAddress(new Address("country", "city", "county", "street"));
        Stock stock = new Stock();
        stock.setLocation(location);
        when(locationStrategy.getStockForProduct(any())).thenReturn(stock);
    }

    private List<OrderDetailDto> getOrderDetailsDtoList() {
        List<OrderDetailDto> orderDetailDtos = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            orderDetailDtos.add(new OrderDetailDto(i, i));
        }
        return orderDetailDtos;
    }

    @Test
    public void getLocationsForOrderTest() {
        LocationService locationService = new LocationService(locationStrategy);
        locationService.getLocationsForOrder(getOrderDetailsDtoList()).
            parallelStream().
            forEach(location1 -> {
                assertEquals("Location id", location.getId(), location1.getId());
                assertEquals("Location name", location.getName(), location1.getName());
                assertEquals("Location address", location.getAddress(), location1.getAddress());
            });


    }
}
