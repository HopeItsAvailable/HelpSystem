package helpSystem;


import java.util.ArrayList;
import java.util.List;

public class oneTimePasswordGeneratorList {
    private oneTimePasswordGenerator head; //is node
    private int size; //num of passwords out at once
    
    public oneTimePasswordGeneratorList() { //constructor
    	head = null;
    	size = 0;
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
                	removePassword(previous, current);
                    return false; //code Expired
                } else {
                	removePassword(previous, current); // Remove after use
                    return true; //still valid
                }
            }
            previous = current;
            current = current.next;
        }
        return false; 
    }

    
    private void removePassword(oneTimePasswordGenerator previous, oneTimePasswordGenerator current) {
        if (previous == null) {
            head = current.next;
        } else {
            previous.next = current.next;
        }
    }
}
