package ro.msg.learning.shop.configurations;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ro.msg.learning.shop.distance_APIs.DistanceAPI;
import ro.msg.learning.shop.distance_APIs.GoogleDistanceMatrixAPI;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class DistanceAPIConfiguration {


    @Value("${online-shop.api-key:}")
    private String apiKey;


    private final RestTemplate restTemplate;

    @Bean
    public DistanceAPI distanceAPI() {
        log.info("Running with apiKey: {}", apiKey);
        return new GoogleDistanceMatrixAPI(apiKey, restTemplate);
    }
}
