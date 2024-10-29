package helpSystem;

import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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
	            + "role ENUM('admin', 'student', 'instructor')"  // Role limited to these values
	            + ")";
	    statement.execute(userTable);
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
	            String role = "admin";

	            // Encrypt password
	            String encryptedPassword = Base64.getEncoder().encodeToString(
	                encryptionHelper.encrypt(password.getBytes(), EncryptionUtils.getInitializationVector(email.toCharArray()))
	            );

	            // Insert admin user into the database
	            String insertUser = "INSERT INTO cse360users (email, password, role) VALUES (?, ?, ?)";
	            try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
	                pstmt.setString(1, email);
	                pstmt.setString(2, encryptedPassword);
	                pstmt.setString(3, role);
	                pstmt.executeUpdate();
	            }
	        } else {
	            throw new Exception("Admin user already exists. Use invite code for subsequent users.");
	        }
	    }
	}

	// Method to register users after the admin with an invite code
	public void registerWithInviteCode(String email, String password, String inviteCode) throws Exception {
	    // Check if table already has users
	    String countQuery = "SELECT COUNT(*) FROM cse360users";
	    try (Statement stmt = connection.createStatement();
	         ResultSet rs = stmt.executeQuery(countQuery)) {
	        
	        rs.next();
	        int userCount = rs.getInt(1);

	        // If no users exist, throw an error (admin should be created first)
	        if (userCount == 0) {
	            throw new Exception("No admin user found. Admin must register first.");
	        } else {
	            // Validate invite code
	            if (!validateInviteCode(inviteCode)) {
	                throw new Exception("Invalid invite code.");
	            }

	            // Assign default role (e.g., 'student') or a role based on the invite code
	            String role = "student"; // Modify this if your invite code specifies roles

	            // Encrypt password
	            String encryptedPassword = Base64.getEncoder().encodeToString(
	                encryptionHelper.encrypt(password.getBytes(), EncryptionUtils.getInitializationVector(email.toCharArray()))
	            );

	            // Insert user into the database
	            String insertUser = "INSERT INTO cse360users (email, password, role) VALUES (?, ?, ?)";
	            try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
	                pstmt.setString(1, email);
	                pstmt.setString(2, encryptedPassword);
	                pstmt.setString(3, role);
	                pstmt.executeUpdate();
	            }
	        }
	    }
	}



	public boolean login(String email, String password, String role) throws Exception {
		String encryptedPassword = Base64.getEncoder().encodeToString(
				encryptionHelper.encrypt(password.getBytes(), EncryptionUtils.getInitializationVector(email.toCharArray()))
		);	
		
		String query = "SELECT * FROM cse360users WHERE email = ? AND password = ? AND role = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, email);
			pstmt.setString(2, encryptedPassword);
			pstmt.setString(3, role);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		}
	}
	
	public boolean doesUserExist(String email) {
	    String query = "SELECT COUNT(*) FROM cse360users WHERE email = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        
	        pstmt.setString(1, email);
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

	public void displayUsersByAdmin() throws Exception{
		String sql = "SELECT * FROM cse360users"; 
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql); 

		while(rs.next()) { 
			// Retrieve by column name 
			int id  = rs.getInt("id"); 
			String  email = rs.getString("email"); 
			String role = rs.getString("role");  
			String encryptedPassword = rs.getString("password"); 
			char[] decryptedPassword = EncryptionUtils.toCharArray(
					encryptionHelper.decrypt(
							Base64.getDecoder().decode(
									encryptedPassword
							), 
							EncryptionUtils.getInitializationVector(email.toCharArray())
					)	
			);

			// Display values 
			System.out.print("ID: " + id); 
			System.out.print(", Email: " + email); 
			System.out.print(", Password: "); 
			EncryptionUtils.printCharArray(decryptedPassword);
			System.out.println(", Role: " + role); 
			
			Arrays.fill(decryptedPassword, '0');
		} 
	}
	
	public void displayUsersByUser() throws Exception{
		String sql = "SELECT * FROM cse360users"; 
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql); 

		while(rs.next()) { 
			// Retrieve by column name 
			int id  = rs.getInt("id"); 
			String  email = rs.getString("email"); 
			String role = rs.getString("role");  
			String encryptedPassword = rs.getString("password"); 
			char[] decryptedPassword = EncryptionUtils.toCharArray(
					encryptionHelper.decrypt(
							Base64.getDecoder().decode(
									encryptedPassword
							), 
							EncryptionUtils.getInitializationVector(email.toCharArray())
					)	
			);

			// Display values 
			System.out.print("ID: " + id); 
			System.out.print(", Email: " + email); 
			System.out.print(", Password: "); 
			EncryptionUtils.printCharArray(decryptedPassword);
			System.out.println(", Role: " + role); 
			
			Arrays.fill(decryptedPassword, '0');
		} 
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
