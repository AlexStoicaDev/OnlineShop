package ro.msg.learning.shop.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supplier")
    // @NotNull
    private List<Product> products;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // @NotNull
    private String name;


}
