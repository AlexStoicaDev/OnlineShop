package ro.msg.learning.shop.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Revenue {
    @Id
    @GeneratedValue
    private Integer id;

    @JsonIgnore
    @ManyToOne
    private Location location;

    private LocalDateTime date;

    private BigDecimal sum;

}
