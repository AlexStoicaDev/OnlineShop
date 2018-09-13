package ro.msg.learning.shop.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    @ManyToOne
    private ProductCategory category;

    @ManyToOne
    private Supplier supplier;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<Stock> stocks;


    @OneToMany(cascade = CascadeType.ALL,mappedBy ="product")
    private List<OrderDetail> orderDetails;

    @GeneratedValue
    @Id
    private Integer id;

    private String name;

    private String description;

    private BigDecimal price;

    private Double weight;
}
