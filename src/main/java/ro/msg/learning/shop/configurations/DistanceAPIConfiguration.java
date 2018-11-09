package ro.msg.learning.shop.configurations;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ro.msg.learning.shop.distance_apis.DistanceAPI;
import ro.msg.learning.shop.distance_apis.GoogleDistanceMatrixAPI;

@Slf4j
@Configuration
@RequiredArgsConstructor
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



