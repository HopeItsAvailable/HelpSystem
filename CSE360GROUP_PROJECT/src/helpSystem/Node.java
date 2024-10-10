package helpSystem;

public class Node {
	final public int TOTALNUMBEROFROLES = 3; //Number of roles. Currently admin, student, instructor.
    String username;
    String password;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String preferredFirstName;
    private int numOfRoles;
    
    Node next;
    
    private boolean isAdmin;     // Boolean for admin role
    private boolean isStudent;   // Boolean for student role
    private boolean isInstructor;// Boolean for instructor role

    //used on initial setup screen. Set methods are used to store other fields besides username/password
    //when the user reaches account finalization screen.
    public Node(String username, String password) {
        this.username = username;
        this.password = password;
        this.next = null;
        this.email = null;
        this.firstName = null;
        this.middleName = null;
        this.lastName = null;
        this.preferredFirstName = null;
        this.numOfRoles = 0;
     
        //booleans to see what roles this user has
        this.isAdmin = false;
        this.isStudent = false;
        this.isInstructor = false;
         
    }
    
    public void addOneToRole() {
    	this.numOfRoles++;
    }
    
    public void minusOneToRole() {
    	this.numOfRoles--;
    }
    
    //GET METHODS
    
    public int getNumOfRoles() {
    	return this.numOfRoles;
    }
    
    public boolean getIsAdmin() {
    	return this.isAdmin;
    }
    
    public boolean getIsStudent() {
    	return this.isStudent;
    }
    
    public boolean getIsInstructor() {
    	return this.isInstructor;
    }
    public String getPassword() {
    	return this.password;
    }
    public String getEmail() {
    	return this.email;
    }
    
    
    
    // Setter methods to update additional fields later
    
    public void setPassword(String password) {
        this.password = password;
    }
    
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
    
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setStudent(boolean isStudent) {
        this.isStudent = isStudent;
    }

    public void setInstructor(boolean isInstructor) {
        this.isInstructor = isInstructor;
    }


    //@Override
    public String toString() {
        return "Username: " + username + ", Password: " + password + ", Email: " + email 
                + "\n Full Name: " + firstName + " " + middleName + " " + lastName 
                + ", Preferred First Name: " + preferredFirstName
                + "\n Roles: " + (isAdmin ? "Admin " : "") 
                + (isStudent ? "Student " : "") 
                + (isInstructor ? "Instructor " : "\n\n");
    }
}
