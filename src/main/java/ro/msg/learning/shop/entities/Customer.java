package ro.msg.learning.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ro.msg.learning.shop.validators.PasswordValidator;
import ro.msg.learning.shop.validators.UsernameValidator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Customer {

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private List<Order> orders;


    @Id
    @GeneratedValue
    private Integer id;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;


    @Setter(AccessLevel.NONE)
    private String userName;

    @Setter(AccessLevel.NONE)
    private String password;

    public void setPassword(@NotNull String password) {
        PasswordValidator.validate(password);
        this.password = new BCryptPasswordEncoder().encode(password);
    }


    public void setUserName(@NotNull String userName) {
        UsernameValidator.validate(userName);
        this.userName = userName;
    }

    @ManyToMany
    @JoinTable(name = "customer_role",
        joinColumns = @JoinColumn(name = "customer_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> customerRoles;

}
