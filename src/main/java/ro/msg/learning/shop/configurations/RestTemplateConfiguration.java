package ro.msg.learning.shop.configurations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RestTemplateConfiguration {

    @Value("${online-shop.proxy.status:false}")
    private boolean active;
    @Value("${online-shop.proxy.name:}")
    private String proxy;
    @Value("${online-shop.proxy.port:0}")
    private int port;


    @Bean
    public RestTemplate restTemplate() {
        if (active) {
            log.info("Running with active request proxy: {}:{}", proxy, port);
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy, port)));
            return new RestTemplate(requestFactory);
        }

        return new RestTemplate();
    }

}
