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
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LocationServiceTest {
    @Mock
    private LocationStrategy locationStrategy;

    private Stock stock;
    private Location location;

    @Before
    public void before() {
        location = new Location();
        location.setId(1);
        location.setName("test name");
        location.setAddress(new Address("country", "city", "county", "street"));
        stock = new Stock();
        stock.setLocation(location);
        when(locationStrategy.getStockForProduct(any())).thenReturn(stock);
    }

    private List<OrderDetailDto> getOrderDetailsDtoList() {
        List<OrderDetailDto> orderDetailDtos = new ArrayList<>();
        Random r = new Random();
        int nrOfElements = r.nextInt() % 1000;
        while (nrOfElements > 0) {
            orderDetailDtos.add(new OrderDetailDto(r.nextInt(), r.nextInt()));
            nrOfElements--;
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
