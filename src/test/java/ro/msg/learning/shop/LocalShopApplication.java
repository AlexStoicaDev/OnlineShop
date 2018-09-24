package ro.msg.learning.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class LocalShopApplication {
    public static void main(String[] args) {
        SpringApplication shopApplication=new SpringApplication(ShopApplication.class);
        shopApplication.run(args);

    }
}
