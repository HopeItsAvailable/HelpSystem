package helpSystem;


import java.util.ArrayList;
import java.util.List;

public class oneTimePasswordGeneratorList {
    private oneTimePasswordGenerator head; //is node
    private int size; //num of passwords out at once
    private boolean alreadyUsed;
    private boolean oneTime;
    
    public oneTimePasswordGeneratorList() { //constructor
    	head = null;
    	size = 0;
    	
    	alreadyUsed = false; //flag to indicate one time code
    	oneTime = false;
    }

    public void addPassword(String Code, String[] roles, long expirationTime) {
    	oneTimePasswordGenerator newNode = new oneTimePasswordGenerator(Code, roles, expirationTime);
        if (head == null) {
            head = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }
    }
    
    public void addPasswordForReset(String Code, long expirationTime) {
    	oneTimePasswordGenerator newNode = new oneTimePasswordGenerator(Code, expirationTime);
    	this.oneTime = true; //since the password is for reset, set the flag to false and check in main to send user to right page
        if (head == null) {
            head = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }
    }
    
    public boolean getFlag(String code) { //check if a code is one-time
    	return this.oneTime;
    }
    

    //check if the password is still valid.
    public boolean validatePassword(String code) {
    	oneTimePasswordGenerator current = head;
    	oneTimePasswordGenerator previous = null;

        while (current != null) {
            if (current.code.equals(code)) {
                if (System.currentTimeMillis() > current.expirationTime) {
                	//removePassword(previous, current); NEED TO PRINT WITH USERS?
                	System.out.println("False1");
                    return false; //code Expired
                } else {
                	//removePassword(previous, current); // Remove after use
                	alreadyUsed = true;
                    return true; //still valid
                }
            }
            previous = current;
            current = current.next;
        }
        System.out.println("False2");
        return false; 
    }
    
    public String[] validatePassword1(String code) {
        oneTimePasswordGenerator current = head;
        oneTimePasswordGenerator previous = null;

        while (current != null) {
            if (current.code.equals(code)) {
                if (System.currentTimeMillis() > current.expirationTime) {
                    return null; // OTP expired
                } else {
                    String[] roles = current.roles; // Grab roles associated with this OTP
                    return roles; // OTP is valid, return roles
                }
            }
            previous = current;
            current = current.next;
        }
        return null; 
    }
    
    public String[] getRolesFromOTP(String code) {
    	oneTimePasswordGenerator current = head; // Assuming head is the start of your linked list
	    while (current != null) {
	        if (current.getCode().equals(code) && current.getExpirationTime() > System.currentTimeMillis()) {
	            return current.getRoles(); // Return the roles if OTP is valid
	        }
	        current = current.next; // Move to the next node
	    }
	    return null; // Return null if the OTP is invalid or expired
	}

    
    private void removePassword(oneTimePasswordGenerator previous, oneTimePasswordGenerator current) {
        if (previous == null) {
            head = current.next;
        } else {
            previous.next = current.next;
        }
    }
}
