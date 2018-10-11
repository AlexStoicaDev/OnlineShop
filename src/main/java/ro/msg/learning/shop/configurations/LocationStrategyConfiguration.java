package ro.msg.learning.shop.configurations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ro.msg.learning.shop.exceptions.StrategyNotFoundException;
import ro.msg.learning.shop.repositories.StockRepository;
import ro.msg.learning.shop.strategies.ClosestLocationStrategy;
import ro.msg.learning.shop.strategies.LocationStrategy;
import ro.msg.learning.shop.strategies.SingleLocationStrategy;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class LocationStrategyConfiguration {

    @Value("${online-shop.strategy}")
    private String strategy;

    @Value("${online-shop.api-key}")
    private String apiKey;

    private final StockRepository stockRepository;
    private final RestTemplate restTemplate;

    @Bean
    public LocationStrategy locationStrategy() {

        if (strategy.equalsIgnoreCase("single")) {
            // return new SingleLocationStrategy(stockRepository);
            return new SingleLocationStrategy(stockRepository);
        }
        if (strategy.equalsIgnoreCase("closest")) {
            return new ClosestLocationStrategy(stockRepository, restTemplate, apiKey);
        }
        log.error("No strategy with this name was found", strategy);
        throw new StrategyNotFoundException("No strategy with this name was found", strategy);
    }


}
