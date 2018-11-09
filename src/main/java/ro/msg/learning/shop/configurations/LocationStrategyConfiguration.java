package ro.msg.learning.shop.configurations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.msg.learning.shop.distance_apis.DistanceAPI;
import ro.msg.learning.shop.exceptions.StrategyNotFoundException;
import ro.msg.learning.shop.repositories.LocationRepository;
import ro.msg.learning.shop.repositories.ProductRepository;
import ro.msg.learning.shop.repositories.StockRepository;
import ro.msg.learning.shop.strategies.LocationStrategy;
import ro.msg.learning.shop.strategies.ShortestLocationPathStrategy;
import ro.msg.learning.shop.strategies.SingleLocationStrategy;


/**
 * Configuration for location strategy(strategy used to find locations for order)
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class LocationStrategyConfiguration {

    @Value("${online-shop.strategy:single}")
    private String strategy;


    private final ProductRepository productRepository;
    private final LocationRepository locationRepository;
    private final StockRepository stockRepository;
    private final DistanceAPI distanceAPI;


    @Bean
    public LocationStrategy locationStrategy() {


        log.info("Running with location strategy: {}", strategy);
        if (strategy.equalsIgnoreCase("single")) {
            return new SingleLocationStrategy(locationRepository, productRepository);
        }
        if (strategy.equalsIgnoreCase("path")) {
            return new ShortestLocationPathStrategy(stockRepository, locationRepository, distanceAPI);
        }
        log.error("No strategy with this name was found", strategy);
        throw new StrategyNotFoundException("No strategy with this name was found", strategy);
    }


}
