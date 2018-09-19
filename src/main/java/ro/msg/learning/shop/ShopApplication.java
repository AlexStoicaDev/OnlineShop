package ro.msg.learning.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@EntityScan(
    basePackageClasses = {ShopApplication.class, Jsr310JpaConverters.class}
)
@SpringBootApplication
public class ShopApplication {


    public static void main(String[] args) {

        SpringApplication.run(ShopApplication.class, args);


    }
}
