package ro.msg.learning.shop.dtos.Customers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDtoIn {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private List<Integer> roleIds;

}
