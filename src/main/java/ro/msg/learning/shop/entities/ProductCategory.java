package ro.msg.learning.shop.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductCategory {

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "category")
    private List<Product> products=new LinkedList<>();

    @GeneratedValue
    @Id
    private Integer id;

    private String mame;

    private String description;


}
