package helpSystem;


import java.util.ArrayList;
import java.util.List;

public class oneTimePasswordGeneratorList {
    private oneTimePasswordGenerator head; //is node
    private int size; //num of passwords out at once
    private boolean alreadyUsed;
    
    public oneTimePasswordGeneratorList() { //constructor
    	head = null;
    	size = 0;
    	alreadyUsed = false; //flag to indicate one time code
    }

    public void addPassword(String code, String[] roles, long expirationTime) {
    	oneTimePasswordGenerator newNode = new oneTimePasswordGenerator(code, roles, expirationTime);
        if (head == null) {
            head = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }
    }

    //check if the password is still valid.
    public boolean validatePassword(String code) {
    	oneTimePasswordGenerator current = head;
    	oneTimePasswordGenerator previous = null;

        while (current != null) {
            if (current.code.equals(code)) {
                if (System.currentTimeMillis() > current.expirationTime) {
                	//removePassword(previous, current); NEED TO PRINT WITH USERS?
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

    
    private void removePassword(oneTimePasswordGenerator previous, oneTimePasswordGenerator current) {
        if (previous == null) {
            head = current.next;
        } else {
            previous.next = current.next;
        }
    }
}
