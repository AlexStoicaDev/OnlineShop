package ro.msg.learning.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    @Column(unique = true)
    private String username;

    @Setter(AccessLevel.NONE)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "customer_role",
        joinColumns = @JoinColumn(name = "customer_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> customerRoles;

    public void setPassword(@NotNull String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }


}
