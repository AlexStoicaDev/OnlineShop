package ro.msg.learning.shop.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import ro.msg.learning.shop.entities.embeddables.Address;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "orders")

public class Order {

    @ManyToOne
    //@NotNull
    private Customer customer;

    @NotNull
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private List<OrderDetail> orderDetails;


    //will replace it with mapped by
    // @NotNull
    @ManyToMany
    @JoinTable(name = "orders_location",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "location_id"))
    private List<Location> locations;

    @Id
    @GeneratedValue
    private Integer id;


    private LocalDateTime orderDate;

    //@NotNull
    @Embedded
    private Address address;
}
