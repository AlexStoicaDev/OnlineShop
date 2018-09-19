package ro.msg.learning.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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


    @JsonIgnore
    @ManyToOne
   // @NotNull
    private Product product;

    @JsonIgnore
    @ManyToOne
   // @NotNull
    private Location location;

    @Id
    @GeneratedValue
    private Integer id;

   // @NotNull
    private Integer quantity;

}
