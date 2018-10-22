package ro.msg.learning.shop.configurations;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ro.msg.learning.shop.entities.Customer;

import java.util.Collection;
import java.util.stream.Collectors;


public class MyUserDetails implements UserDetails {

    private Customer customer;


    MyUserDetails(Customer customer) {

        this.customer = customer;

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
