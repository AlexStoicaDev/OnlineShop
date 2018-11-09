package ro.msg.learning.shop.entities.embeddables;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String country;
    private String city;
    private String county;
    private String street;
}
