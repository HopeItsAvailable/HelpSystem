//package helpSystem;
//
//public class LinkedList {
//    protected Node head;
//    private int size;
//
//    
//    public LinkedList() {
//    	this.head=null;
//    	this.size = 0;
//    }
//    
//    // Method to add a node at the end of the list
//    public void add(String username, String password) {
//        Node newNode = new Node(username, password);
//
//        if (head == null) {
//            head = newNode;
//            size++;
//            System.out.println(username + "ADD()");
//        } 
//        
//        else {
//            Node current = head;
//            while (current.next != null) {
//                current = current.next;
//            }
//            current.next = newNode;
//            size++;
//        }
//        
//    }
//    
//    public void changeUserPassword(String username, String password) {
//    	Node user = searchByUsername(username);
//    	if (user != null) {
//            user.setPassword(password);
//            System.out.println("Account for " + username + " changed.");
//        } else {
//            System.out.println("User with username '" + username + "' not found.");
//        }
//    }
//    
//    public void finalizeUserEmail(String username, String email) {
//    	Node user = searchByUsername(username);
//    	if (user != null) {
//            user.setEmail(email);
//            System.out.println("Account for " + username + " finalized.");
//        } else {
//            System.out.println("User with username '" + username + "' not found.");
//        }
//    	
//    }
//    
//    public void finalizeUserFirstName(String username, String firstName) {
//        Node user = searchByUsername(username);
//        if (user != null) {
//            user.setFirstName(firstName);
//            System.out.println("First name for " + username + " updated.");
//        } else {
//            System.out.println("User with username '" + username + "' not found.");
//        }
//    }
//    
//    public void finalizeUserMiddleName(String username, String middleName) {
//        Node user = searchByUsername(username);
//        if (user != null) {
//            user.setMiddleName(middleName);
//            System.out.println("Middle name for " + username + " updated.");
//        } else {
//            System.out.println("User with username '" + username + "' not found.");
//        }
//    }
//
//    // Method to finalize the user's last name
//    public void finalizeUserLastName(String username, String lastName) {
//        Node user = searchByUsername(username);
//        if (user != null) {
//            user.setLastName(lastName);
//            System.out.println("Last name for " + username + " updated.");
//        } else {
//            System.out.println("User with username '" + username + "' not found.");
//        }
//    }
//
//    // Method to finalize the user's preferred first name
//    public void finalizeUserPreferredFirstName(String username, String preferredFirstName) {
//        Node user = searchByUsername(username);
//        if (user != null) {
//            user.setPreferredFirstName(preferredFirstName);
//            System.out.println("Preferred first name for " + username + " updated.");
//        } else {
//            System.out.println("User with username '" + username + "' not found.");
//        }
//    }
//    
//    public void addRoleToUser(String username, String role) {
//        Node user = searchByUsername(username);
//        if (user != null) {
//            switch (role.toLowerCase()) {
//                case "admin":
//                    user.setAdmin(true);
//                    System.out.println("Role Admin added to user " + username + ".");
//                    user.addOneToRole();
//                    break;
//                case "student":
//                    user.setStudent(true);
//                    System.out.println("Role Student added to user " + username + ".");
//                    user.addOneToRole();
//                    break;
//                case "instructor":
//                    user.setInstructor(true);
//                    System.out.println("Role Instructor added to user " + username + ".");
//                    user.addOneToRole(); 
//                    break;
//                default:
//                    System.out.println("Role '" + role + "' is not recognized.");
//                    break;
//            }
//        } else {
//            System.out.println("User with username '" + username + "' not found.");
//        }
//    }
//    
//    public void removeRoleToUser(String username, String role) {
//        Node user = searchByUsername(username);
//        if (user != null) {
//            switch (role.toLowerCase()) {
//                case "admin":
//                    user.setAdmin(false);
//                    System.out.println("Role Admin removed from user " + username + ".");
//                    user.minusOneToRole();
//                    break;
//                case "student":
//                    user.setStudent(false);
//                    System.out.println("Role Student removed from user " + username + ".");
//                    user.minusOneToRole();
//                    break;
//                case "instructor":
//                    user.setInstructor(false);
//                    System.out.println("Role Instructor removed from user " + username + ".");
//                    user.minusOneToRole(); 
//                    break;
//                default:
//                    System.out.println("Role '" + role + "' is not recognized.");
//                    break;
//            }
//        } else {
//            System.out.println("User with username '" + username + "' not found.");
//        }
//    }
//
//    // Method to display the linked list
//    public String display() {
//        if (head == null) {
//        	return "The list is empty."; 
//        }
//        String toReturn = "";
//        Node current = head;
//        while (current != null) {
//            toReturn += current.toString();
//            current = current.next;
//        }
//        return toReturn;
//        
//    }
//    
//
//    
//    public void resetAccount(String Username) {
//    	
//    }
//    
//    public void deleteAccount(String Username) {
//    	if (head == null) {
//            System.out.println("List is empty. No users to delete.");
//            return;
//        }
//
//        Node current = head;
//        Node previous = null;
//
//        while (current != null) {
//            if (current.username.equals(Username)) {
//            	//delete node from head
//                if (previous == null) {
//                    head = current.next; // move head to one ahead
//                } else {
//                    previous.next = current.next; // move next to the next next
//                }
//                System.out.println("User " + Username + " has been deleted.");
//                return;
//            }
//            previous = current;
//            current = current.next;
//        }
//
//        System.out.println("User " + Username + " does not exist.");
//    }
//    
//    
//    public void inviteUser(String email) {
//    	
//    }
//    
//    public boolean checkIfAdmin() {
//        Node current = head;
//        while (current != null) {
//            if (current.getIsAdmin()) {
//                return true; 
//            }
//            current = current.next;
//        }
//        return false; 
//    }
//    
//    public boolean isAdminFinalized() {
//        Node current = head;
//        while (current != null) {
//        	//check if all fields have something. Only check email bc if email has soemthing, 
//        	//everything will
//            if (current.getIsAdmin() && current.getEmail() != null && !current.getEmail().isEmpty()) { 
//                return true; 
//            }
//            current = current.next;
//        }
//        return false; // No finalized admin found
//    }
//    
//    public int getNumOfRoles(String username) {
//        Node user = searchByUsername(username);
//        return user.getNumOfRoles();
//    }
//    
//    public boolean isAdmin(String username) {
//    	Node user = searchByUsername(username);
//        return user.getIsAdmin();
//
//    }
//    
//    public boolean isStudent(String username) {
//    	Node user = searchByUsername(username);
//    	return user.getIsStudent();
//    }
//    
//    public boolean isInstructor(String username) {
//    	Node user = searchByUsername(username);
//    	return user.getIsInstructor();
//    }
//
//    // Method to search for a node by username
//    public Node searchByUsername(String username) {
//        Node current = head;
//        while (current != null) {
//            if (current.username.equals(username)) {
//                return current;
//            }
//            current = current.next;
//        }
//        return null;
//    }
//
//	public boolean isEmpty() {
//		if (size == 0){
//			return true;
//		}else {
//			return false;
//		}
//	}
//    
//    
//    
//}
package unused;

