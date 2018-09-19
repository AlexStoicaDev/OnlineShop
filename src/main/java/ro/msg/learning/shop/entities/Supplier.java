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
public class Supplier {

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "supplier")
   // @NotNull
    private List<Product> products;

    @GeneratedValue
    @Id
    private Integer id;

   // @NotNull
    private String name;



}
