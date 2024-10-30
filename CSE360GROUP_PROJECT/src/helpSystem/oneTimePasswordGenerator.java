package helpSystem;

import java.util.*;

public class oneTimePasswordGenerator { //linkedList for OTP's
    String code;
    String[] roles; 
    long expirationTime; //expiration date and how long until it is reached
    oneTimePasswordGenerator next;

    //used by the admin to send invite code with associated roles
    public oneTimePasswordGenerator(String code, String[] roles, long expirationTime) {
        this.code = code;
        this.roles = roles;
        this.expirationTime = expirationTime;
        this.next = null;
        
    }

    //used by the admin when they are going to send a reset password code to users
	public oneTimePasswordGenerator(String code, long expirationTime) { 
		this.code = code;
        this.expirationTime = expirationTime;
        this.next = null;
	}
	
	 public String [] getRoles() {
		 return this.roles;
	}
	 
	 public String getCode() {
		 return this.code;
	 }
	 public long getExpirationTime() {
		 return this.expirationTime;
	 }
}