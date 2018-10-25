package ro.msg.learning.shop.configurations;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.msg.learning.shop.exceptions.UserNotFoundException;
import ro.msg.learning.shop.repositories.CustomerRepository;
import ro.msg.learning.shop.user_details.MyUserDetails;


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
            .antMatchers(HttpMethod.GET, "/api/customer").authenticated()
            .antMatchers(HttpMethod.GET, "/api/order").permitAll()
            .antMatchers(HttpMethod.POST, "/api/customer").permitAll()
            .antMatchers(HttpMethod.POST, "/api/order").authenticated()
            .antMatchers("/api/report/**").permitAll()
            .antMatchers("/api/stock/**").permitAll()
            .antMatchers("/**").hasAuthority("ADMIN")
            .and()
            .httpBasic().and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }

}
