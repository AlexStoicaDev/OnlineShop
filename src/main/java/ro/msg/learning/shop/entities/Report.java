package ro.msg.learning.shop.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import ro.msg.learning.shop.wrappers.DateProductIdQuantityTotalRevenueWrapper;

import javax.persistence.Id;
import java.io.ByteArrayInputStream;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Report {
    @Id
    private String id;

    private Integer month;
    private Integer year;

    private List<DateProductIdQuantityTotalRevenueWrapper> dateProductIdQuantityTotalRevenueWrappers;

    private ByteArrayInputStream byteArrayInputStream;
}
