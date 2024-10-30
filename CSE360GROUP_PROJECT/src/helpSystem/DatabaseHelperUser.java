package helpSystem;

import java.sql.*;
import java.util.Base64;

import org.bouncycastle.util.Arrays;

import Encryption.EncryptionHelper;
import Encryption.EncryptionUtils;

class DatabaseHelperUser {

	// JDBC driver name and database URL 
	static final String JDBC_DRIVER = "org.h2.Driver";   
	static final String DB_URL = "jdbc:h2:~/userDatabase";  

	//  Database credentials 
	static final String USER = "sa"; 
	static final String PASS = ""; 

	private Connection connection = null;
	private Statement statement = null; 
	//	PreparedStatement pstmt
	
	private EncryptionHelper encryptionHelper;
	
	public DatabaseHelperUser() throws Exception {
		encryptionHelper = new EncryptionHelper();
		
	}

	public void connectToDatabase() throws SQLException {
		try {
			Class.forName(JDBC_DRIVER); // Load the JDBC driver
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement(); 
			createTables();  // Create the necessary tables if they don't exist
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver not found: " + e.getMessage());
		}
	}

	
	private void createTables() throws SQLException {
	    String userTable = "CREATE TABLE IF NOT EXISTS cse360users ("
	            + "id INT AUTO_INCREMENT PRIMARY KEY, "  // Unique ID for each user
	            + "username VARCHAR(50), "       // Username, must not be null
	            + "password VARCHAR(255), "      // Password, hashed and stored
	            + "email VARCHAR(255) UNIQUE, "  // Email must be unique and not null
	            + "firstName VARCHAR(50), "               // First name
	            + "middleName VARCHAR(50), "              // Middle name, optional
	            + "lastName VARCHAR(50), "       // Last name, must not be null
	            + "preferredFirstName VARCHAR(50), "      // Preferred first name, optional
	            + "isAdmin BOOLEAN DEFAULT FALSE, "  // Boolean indicating if the user is an admin
	            + "isStudent BOOLEAN DEFAULT FALSE, "  // Boolean indicating if the user is a student
	            + "isInstructor BOOLEAN DEFAULT FALSE)";
	           
	    statement.execute(userTable);
	}
	
	public boolean[] getUserRoles(String email) throws SQLException {
	    String query = "SELECT isAdmin, isStudent, isInstructor FROM cse360users WHERE email = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, email);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                boolean[] roles = new boolean[3];
	                roles[0] = rs.getBoolean("isAdmin");        // Admin role
	                roles[1] = rs.getBoolean("isStudent");      // Student role
	                roles[2] = rs.getBoolean("isInstructor");   // Instructor role
	                return roles;
	            } else {
	                return null;
	            }
	        }
	    }
	}

	
	public void deleteUser(String username) throws SQLException {
		if(doesUserExist(username)) {
			
			String deleteSQL = "DELETE FROM cse360Articles WHERE email = ?";
		    
		    try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)){
		    	
		    	pstmt.setString(1, username);
		   	
		    	pstmt.executeUpdate();
	            
		    }
		}
    }



	// Check if the database is empty
	public boolean isDatabaseEmpty() throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM cse360users";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			return resultSet.getInt("count") == 0;
		}
		return true;
	}

//	public void register(String email, String password, String role) throws Exception {
//		String encryptedPassword = Base64.getEncoder().encodeToString(
//				encryptionHelper.encrypt(password.getBytes(), EncryptionUtils.getInitializationVector(email.toCharArray()))
//		);
//		
//		
//		
//		String insertUser = "INSERT INTO cse360users (email, password, role) VALUES (?, ?, ?)";
//		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
//			pstmt.setString(1, email);
//			pstmt.setString(2, encryptedPassword);
//			pstmt.setString(3, role);
//			pstmt.executeUpdate();
//		}
//	}
	
	// Method to register the first user (admin) without an invite code
	public void registerFirstUser(String email, String password) throws Exception {
	    // Check if table is empty
	    String countQuery = "SELECT COUNT(*) FROM cse360users";
	    try (Statement stmt = connection.createStatement();
	         ResultSet rs = stmt.executeQuery(countQuery)) {

	        rs.next();
	        int userCount = rs.getInt(1);

	        // If no users exist, register the first user as 'admin'
	        if (userCount == 0) {
	            // Encrypt password
	            String encryptedPassword = Base64.getEncoder().encodeToString(
	                encryptionHelper.encrypt(password.getBytes(), EncryptionUtils.getInitializationVector(email.toCharArray()))
	            );

	            // Insert admin user into the database with the isAdmin role set to true
	            String insertUser = "INSERT INTO cse360users (email, password, isAdmin, isStudent, isInstructor) VALUES (?, ?, ?, ?, ?)";
	            try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
	                pstmt.setString(1, email);
	                pstmt.setString(2, encryptedPassword);
	                pstmt.setBoolean(3, true);  // isAdmin = true for the first user
	                pstmt.setBoolean(4, false); // isStudent = false
	                pstmt.setBoolean(5, false); // isInstructor = false
	                pstmt.executeUpdate();
	            }
	        } else {
	            throw new Exception("Admin user already exists. Use invite code for subsequent users.");
	        }
	    }
	}

	
	

	// Method to register users after the admin with an invite code
	public void registerWithInviteCode(String username, String password, String[] roles) throws Exception {
	    // Check if table already has users
	    String countQuery = "SELECT COUNT(*) FROM cse360users";
	    try (Statement stmt = connection.createStatement();
	         ResultSet rs = stmt.executeQuery(countQuery)) {

	        rs.next();
	        int userCount = rs.getInt(1);

	        // Encrypt password
	        String encryptedPassword = Base64.getEncoder().encodeToString(
	            encryptionHelper.encrypt(password.getBytes(), EncryptionUtils.getInitializationVector(username.toCharArray()))
	        );

	        // Insert user into the database
	        String insertUser = "INSERT INTO cse360users (username, password, isAdmin, isStudent, isInstructor) VALUES (?, ?, ?, ?, ?)";

	        try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
	            pstmt.setString(1, username);
	            pstmt.setString(2, encryptedPassword);

	            // Set the role booleans based on the roles array
	            boolean isAdmin = false;
	            boolean isStudent = false;
	            boolean isInstructor = false;

	            for (String role : roles) {
	                if (role.equalsIgnoreCase("admin")) {
	                    isAdmin = true;
	                } else if (role.equalsIgnoreCase("student")) {
	                    isStudent = true;
	                } else if (role.equalsIgnoreCase("instructor")) {
	                    isInstructor = true;
	                }
	            }

	            pstmt.setBoolean(3, isAdmin);
	            pstmt.setBoolean(4, isStudent);
	            pstmt.setBoolean(5, isInstructor);

	            pstmt.executeUpdate();
	        }
	    }
	}


	
	public void changeUserPassword(String username, String newPassword) {
		
	}


	public boolean login(String username, String password, String role) throws Exception {
		String encryptedPassword = Base64.getEncoder().encodeToString(
				encryptionHelper.encrypt(password.getBytes(), EncryptionUtils.getInitializationVector(username.toCharArray()))
		);	
		
		String query = "SELECT * FROM cse360users WHERE username = ? AND password = ? AND role = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
			pstmt.setString(2, encryptedPassword);
			pstmt.setString(3, role);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		}
	}
	
	public boolean doesUserExist(String username) {
	    String query = "SELECT COUNT(*) FROM cse360users WHERE username = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        
	        pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            // If the count is greater than 0, the user exists
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // If an error occurs, assume user doesn't exist
	}

	public String displayUsersByAdmin() throws Exception{
		String sql = "SELECT * FROM cse360users"; 
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql); 
		
		StringBuilder userInfo = new StringBuilder();


		while(rs.next()) { 
			// Retrieve by column name 
			int id  = rs.getInt("id"); 
			String  email = rs.getString("email"); 
			String role = rs.getString("role");  

			userInfo.append("Article ID: ").append(email)
            .append(", Role: ").append(role).append("\n\n");
		}
		return userInfo.toString();
	}
	
	public String displayUsersByUser() throws Exception{
		String sql = "SELECT * FROM cse360users"; 
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql); 
		
		StringBuilder userInfo = new StringBuilder();


		while(rs.next()) { 
			// Retrieve by column name 
			int id  = rs.getInt("id"); 
			String  email = rs.getString("email"); 
			String role = rs.getString("role");  

			userInfo.append("Article ID: ").append(email)
            .append(", Role: ").append(role).append("\n\n");
		} 
		return userInfo.toString();
	}
	
	private boolean isTableEmpty() throws SQLException {
	    String countQuery = "SELECT COUNT(*) FROM cse360users";
	    try (Statement stmt = connection.createStatement();
	         ResultSet rs = stmt.executeQuery(countQuery)) {
	        
	        rs.next();
	        int userCount = rs.getInt(1);
	        return userCount == 0;
	    }
	}


	public void closeConnection() {
		try{ 
			if(statement!=null) statement.close(); 
		} catch(SQLException se2) { 
			se2.printStackTrace();
		} 
		try { 
			if(connection!=null) connection.close(); 
		} catch(SQLException se){ 
			se.printStackTrace(); 
		} 
	}

	

}
