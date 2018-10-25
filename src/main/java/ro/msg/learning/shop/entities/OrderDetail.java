package ro.msg.learning.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetail {


    // @NotNull
    @ManyToOne
    private Product product;
    // @NotNull
    @ManyToOne
    @JsonIgnore
    private Order order;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    // @NotNull
    private Integer quantity;
}
