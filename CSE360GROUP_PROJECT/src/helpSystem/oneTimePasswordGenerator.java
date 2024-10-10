package helpSystem;

import java.util.*;

public class oneTimePasswordGenerator { //linkedList for OTP's
    String code;
    String[] roles; 
    long expirationTime; //expiration date and how long until it is reached
    oneTimePasswordGenerator next;

    public oneTimePasswordGenerator(String code, String[] roles, long expirationTime) {
        this.code = code;
        this.roles = roles;
        this.expirationTime = expirationTime;
        this.next = null;
    }
}