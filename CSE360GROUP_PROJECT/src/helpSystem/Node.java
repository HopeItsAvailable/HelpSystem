package helpSystem;

public class Node {
	final private int TOTALNUMBEROFROLES = 3; //Number of roles. Currently admin, student, instructor.
    String username;
    String password;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String preferredFirstName;
    
    Node next;
    
    private String[] roles;  // Array to store roles
    private int numOfRoles; 

    //used on initial setup screen. Set methods are used to store other fields besides username/password
    //when the user reaches account finalization screen.
    public Node(String username, String password) {
        this.username = username;
        this.password = password;
        this.next = null;
        this.username = username;
        this.password = password;
        this.email = null;
        this.firstName = null;
        this.middleName = null;
        this.lastName = null;
        this.preferredFirstName = null;
        this.numOfRoles = 1;
        
        this.roles = new String[TOTALNUMBEROFROLES];  // array with slots for each role
        this.numOfRoles = 0;          
    }

    public void addRole(String role) {
    	if (numOfRoles == roles.length) {
    		System.out.println("Max number of roles reached.");
    		return;
    	}
    	
    	// Check if the user already has that role 
    	for (int i = 0; i < numOfRoles; i++) {
    		if (roles[i].equals(role)) {
    			System.out.println("User " + username + " already has the role " + role + ".");
    			return;
    		}
    	}
    	
    	// Add the new role
    	roles[numOfRoles] = role;
    	numOfRoles++;
    	System.out.println("Role " + role + " added to user " + username + ".");
    }

    // Setter methods to update additional fields later
    
    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPreferredFirstName(String preferredFirstName) {
        this.preferredFirstName = preferredFirstName;
    }


    //@Override
    public String toString() {
    	String displayRoles = "";
        for (int i = 0; i < numOfRoles; i++) {
        	displayRoles += (roles[i]);
           
        }
        return "Username: " + username + ", Password: " + password + ", Email: " + email 
                + ", Full Name: " + firstName + " " + middleName + " " + lastName 
                + ", Preferred First Name: " + preferredFirstName
                + ", Roles: " + displayRoles;
    }
}
