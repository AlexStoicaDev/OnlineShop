package ro.msg.learning.shop.validators;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.exceptions.InvalidPasswordException;

import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PasswordValidatorTest {
    @Autowired
    PasswordValidator passwordValidator;

    @Test(expected = InvalidPasswordException.class)
    public void testValidateWhenPasswordIsInvalid() {

        passwordValidator.validate("abcd");
    }

    @Test
    public void testValidatePasswordWhenPasswordIsValid() {

        try {
            passwordValidator.validate("1aA@aa$$$$$$$$2321312AAAaadaada");
        } catch (Exception e) {
            fail("Exception  thrown");
        }
    }
}
