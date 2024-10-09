package helpSystem;

public class LinkedList {
    protected Node head;
    private int size;
//    private String username;
//    private String password;
//    private String email;
//    private String firstName;
//    private String middleName;
//    private String lastName;
//    private String preferredFirstName;

    
    public LinkedList() {
    	this.head=null;
    	this.size = 0;
    }
    
    // Method to add a node at the end of the list
    public void add(String username, String password) {
        Node newNode = new Node(username, password);

        if (head == null) {
            head = newNode;
            size++;
        } 
        
        else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
            size++;
        }
        
    }
    
    public void finalizeUserEmail(String username, String email) {
    	Node user = searchByUsername(username);
    	if (user != null) {
            user.setEmail(email);
            System.out.println("Account for " + username + " finalized.");
        } else {
            System.out.println("User with username '" + username + "' not found.");
        }
    	
    }
    
    public void finalizeUserFirstName(String username, String firstName) {
        Node user = searchByUsername(username);
        if (user != null) {
            user.setFirstName(firstName);
            System.out.println("First name for " + username + " updated.");
        } else {
            System.out.println("User with username '" + username + "' not found.");
        }
    }
    
    public void finalizeUserMiddleName(String username, String middleName) {
        Node user = searchByUsername(username);
        if (user != null) {
            user.setMiddleName(middleName);
            System.out.println("Middle name for " + username + " updated.");
        } else {
            System.out.println("User with username '" + username + "' not found.");
        }
    }

    // Method to finalize the user's last name
    public void finalizeUserLastName(String username, String lastName) {
        Node user = searchByUsername(username);
        if (user != null) {
            user.setLastName(lastName);
            System.out.println("Last name for " + username + " updated.");
        } else {
            System.out.println("User with username '" + username + "' not found.");
        }
    }

    // Method to finalize the user's preferred first name
    public void finalizeUserPreferredFirstName(String username, String preferredFirstName) {
        Node user = searchByUsername(username);
        if (user != null) {
            user.setPreferredFirstName(preferredFirstName);
            System.out.println("Preferred first name for " + username + " updated.");
        } else {
            System.out.println("User with username '" + username + "' not found.");
        }
    }
    
    public void addRoleToUser(String username, String role) {
        Node user = searchByUsername(username);
        if (user != null) {
            user.addRole(role);  // Call the method in Node to add the role
        } else {
            System.out.println("User with username '" + username + "' not found.");
        }
    }

    // Method to display the linked list
    public void display() {
        if (head == null) {
            System.out.println("The list is empty.");
            return;
        }

        Node current = head;
        while (current != null) {
            System.out.println(current);
            current = current.next;
        }
        System.out.println(size);
        
    }

    // Method to search for a node by username
    public Node searchByUsername(String username) {
        Node current = head;
        while (current != null) {
            if (current.username.equals(username)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }
    
    
    
}