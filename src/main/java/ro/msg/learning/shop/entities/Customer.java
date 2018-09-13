package ro.msg.learning.shop.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Customer {
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "customer")
    private List<Order> order;
    @GeneratedValue
    @Id
    private Integer id;
    private String firstName;
    private String lastName;
    private String userName;
}
