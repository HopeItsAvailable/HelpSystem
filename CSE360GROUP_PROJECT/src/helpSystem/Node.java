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
        this.username = username;
        this.password = password;
        this.email = null;
        this.firstName = null;
        this.middleName = null;
        this.lastName = null;
        this.preferredFirstName = null;
        this.numOfRoles = 1;
     
        this.isAdmin = false;
        this.isStudent = false;
        this.isInstructor = false;
     
         
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
                + ", Full Name: " + firstName + " " + middleName + " " + lastName 
                + ", Preferred First Name: " + preferredFirstName
                + ", Roles: " + (isAdmin ? "Admin " : "") 
                + (isStudent ? "Student " : "") 
                + (isInstructor ? "Instructor " : "");
    }
}
