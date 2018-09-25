package ro.msg.learning.shop.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ro.msg.learning.shop.services.OrderService;


@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {TestContext.class, WebAppContext.class})
@WebAppConfiguration
public class TestOrderController {

    private MockMvc mockMvc;

    @InjectMocks
    OrderService orderService;

    @Test
    public void testCreateOrder() {
//        Order order=new Order();
//        OrderDto orderDto=new OrderDto();
//        when(orderService.createOrder(orderDto)).thenReturn(order);
//        mockMvc.perform(new PostMapping("order/create")).andExpect(status().isOk())
    }
}
