package ro.msg.learning.shop.services;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    private static int workload = 12;

    public String hashPassword(String password_plaintext) {

        return BCrypt.hashpw(password_plaintext, BCrypt.gensalt(workload));

    }


}
