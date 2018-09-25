package ro.msg.learning.shop.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.msg.learning.shop.exceptions.StrategyNotFoundException;
import ro.msg.learning.shop.repositories.StockRepository;
import ro.msg.learning.shop.strategies.LocationStrategy;
import ro.msg.learning.shop.strategies.SingleLocationStrategy;


@Configuration
@RequiredArgsConstructor
public class LocationStrategyConfiguration {

    @Value("${online-shop.strategy:single}")
    private String strategy;


    private final StockRepository stockRepository;


    @Bean
    public LocationStrategy locationStrategy() {

        if (strategy.equalsIgnoreCase("single")) {
            return new SingleLocationStrategy(stockRepository);
        }

        throw new StrategyNotFoundException("No strategy found with", strategy);
    }
}
