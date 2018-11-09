package ro.msg.learning.shop.validators;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.exceptions.InvalidUsernameException;

import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsernameValidatorTest {
    @Autowired
    UsernameValidator usernameValidator;

    @Test(expected = InvalidUsernameException.class)
    public void validateWhenUsernameLenghtIsToShort() {
        usernameValidator.validate("abc");
    }

    @Test(expected = InvalidUsernameException.class)
    public void validateWhenUsernameIsTaken() {
        usernameValidator.validate("admin");
    }

    @Test
    public void validate() {
        try {
            usernameValidator.validate("aaaaaaaaaaaaaaaaaaa");
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
}
