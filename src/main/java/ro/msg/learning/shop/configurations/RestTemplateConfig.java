package ro.msg.learning.shop.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Configuration
@RequiredArgsConstructor
//works on bean also !!
public class RestTemplateConfig {

    @Value("${online-shop.proxy-name}")
    private String proxy;

    @Value("${online-shop.proxy-port}")
    private Integer port;


    @ConditionalOnProperty(name = "online-shop.proxy-status", havingValue = "active")
    @Bean(name = "restTemplate")
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy, port)));
        return new RestTemplate(requestFactory);
    }

}
