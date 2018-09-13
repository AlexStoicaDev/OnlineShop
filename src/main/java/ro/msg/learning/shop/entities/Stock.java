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
public class Stock {


    @ManyToOne
    private Product product;
    @ManyToOne
    private Location location;

    @Id
    @GeneratedValue
    private Integer id;

    private Integer quantity;

}
