package ro.msg.learning.shop.configurations;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.msg.learning.shop.exceptions.UserNotFoundException;
import ro.msg.learning.shop.repositories.CustomerRepository;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final CustomerRepository customerRepository;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {


        auth.userDetailsService(username -> new MyUserDetails(customerRepository.
            findByUsername(username).orElseThrow(() -> new UserNotFoundException("aa", "aa"))));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/customer/profile").authenticated()
            .antMatchers("/customer/user").permitAll()
            .antMatchers("/order/create").authenticated()
            .antMatchers("/stock/location/*").permitAll()
            .antMatchers("/order/testStrategy").permitAll()
            .antMatchers("/order/matrix").permitAll()
//                .antMatchers("/customer/delete").hasAuthority("ADMIN")
            .antMatchers("/**").hasAuthority("ADMIN")
            .and()
            .httpBasic().and()
            .csrf().disable()
        ;

    }

}
