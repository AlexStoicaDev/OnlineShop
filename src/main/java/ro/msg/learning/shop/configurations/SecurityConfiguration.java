package ro.msg.learning.shop.configurations;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.msg.learning.shop.entities.Customer;
import ro.msg.learning.shop.repositories.CustomerRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final CustomerRepository customerRepository;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {


        auth.userDetailsService(username -> new MyUserDetails(customerRepository.
            findByUsername(username)));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http
            .authorizeRequests()
            .antMatchers("/customer/profile").authenticated()
            .antMatchers("/customer/user").permitAll()
            .antMatchers("/customer/delete").hasAuthority("ADMIN")
            .antMatchers(HttpMethod.POST).authenticated()
            .anyRequest().permitAll()
            .and()
            .httpBasic()
            .and()
            .csrf().disable()
        ;

    }
}

class MyUserDetails implements UserDetails {

    private Customer customer;


    MyUserDetails(Optional<Customer> customer) {

        customer.ifPresent(customer1 -> this.customer = customer1);

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return customer.
            getCustomerRoles().
            parallelStream().
            map(role -> role.getName().substring(5)).
            map(SimpleGrantedAuthority::new).
            collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return customer.getPassword();
    }

    @Override
    public String getUsername() {
        return customer.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
