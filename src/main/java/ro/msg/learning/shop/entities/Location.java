package ro.msg.learning.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.msg.learning.shop.entities.embeddables.Address;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Location {


    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "location")
    private List<Stock> stocks;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "orders_location",
        joinColumns = @JoinColumn(name = "location_id"),
        inverseJoinColumns = @JoinColumn(name = "order_id"))
    private List<Order> orders;

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    @Embedded
    private Address address;


}
