package helpSystem;

import java.util.*;

public class oneTimePasswordGenerator { //linkedList for OTP's
    String code;
    String role;
    long expirationTime; //expiration date and how long until it is reached
    oneTimePasswordGenerator next;

    public oneTimePasswordGenerator(String code, String[] roles, long expirationTime) {
        this.code = code;
        this.role = role;
        this.expirationTime = expirationTime;
        this.next = null;
    }
}