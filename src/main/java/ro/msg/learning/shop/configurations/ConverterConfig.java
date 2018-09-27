package ro.msg.learning.shop.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import ro.msg.learning.shop.converters.CsvConverter;

/**
 * Configuration for csvConverter
 */
@Configuration
public class ConverterConfig {
    @Bean
    public AbstractHttpMessageConverter csvConverter() {
        return new CsvConverter();
    }
}
