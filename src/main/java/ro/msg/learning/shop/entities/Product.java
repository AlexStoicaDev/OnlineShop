package ro.msg.learning.shop.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @ManyToOne
    private ProductCategory category;

    @ManyToOne
    // @NotNull
    private Supplier supplier;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<Stock> stocks;


    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "product")
    private List<OrderDetail> orderDetails;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // @NotNull
    private String name;

    private String description;

    // @NotNull
    private BigDecimal price;

    private Double weight;
}
