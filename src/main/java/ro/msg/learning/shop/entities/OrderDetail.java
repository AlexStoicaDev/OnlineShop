package ro.msg.learning.shop.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;



@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetail{


   // @NotNull
    @ManyToOne
    private Product product;
   // @NotNull
    @ManyToOne
    private Order order;

    @Id
    @GeneratedValue
    private int id;
   // @NotNull
    private Integer quantity;
}
