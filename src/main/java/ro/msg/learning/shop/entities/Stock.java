package ro.msg.learning.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // @NotNull
    private Integer quantity;

}
