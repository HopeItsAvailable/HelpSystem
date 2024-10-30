package helpSystem;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Base64;
import java.util.Scanner;

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
	
	public boolean[] getUserRoles(String username) throws SQLException {
	    String query = "SELECT isAdmin, isStudent, isInstructor FROM cse360users WHERE username = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, username);
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

	
<<<<<<< HEAD
	
	public void updateUserRoles(String username, boolean[] roles) throws Exception {
	    // Define the SQL update statements for each role
	    String deleteRoleSql = "UPDATE cse360users SET isAdmin = ?, isStudent = ?, isInstructor = ? WHERE username = ?";
	    boolean deleteAdmin = false;
	    boolean deleteStudent = false;
	    boolean deleteInstructor = false;
	    	
	    if(roles[0]==true) {
	    	deleteAdmin = true;
	    }
	    if(roles[1]==true) {
	    	deleteStudent = true;
	    }
	    if(roles[3]==true) {
	    	deleteInstructor = true;
	    }
	    
	    try (PreparedStatement pstmt = connection.prepareStatement(deleteRoleSql)) {
	        // Set the roles to false if they are to be deleted. MUST PUT IN IF STATEMENTS TO SEE IF THEY WERE ALTERED IN THE FIRST PLACE. 
	    	//eg, DONT SET ROLES TO TRUE IF THEY NEVER HAD THEM IN THE FIRST PLACE
	        if(deleteAdmin) {
	        	pstmt.setBoolean(1, !deleteAdmin); // Remove admin role if true, set to false
	        }
	    	if(deleteStudent) {	    		
	    		pstmt.setBoolean(2, !deleteStudent); // Remove student role if true, set to false
	    	}
	    	if(deleteInstructor) {	    		
	    		pstmt.setBoolean(3, !deleteInstructor); // Remove instructor role if true, set to false
	    	}
	        pstmt.setString(4, username);

	        // Execute the update
	        pstmt.executeUpdate();
	    }
	}

	
=======
>>>>>>> branch 'main' of https://github.com/HopeItsAvailable/HelpSystem
	public void deleteUser(String username) throws SQLException {
		if(doesUserExist(username)) {
			
			String deleteSQL = "DELETE FROM cse360users WHERE username = ?";
		    
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
	public void registerFirstUser(String username, String password) throws Exception {
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
	                encryptionHelper.encrypt(password.getBytes(), EncryptionUtils.getInitializationVector(username.toCharArray()))
	            );

	            // Insert admin user into the database with the isAdmin role set to true
	            String insertUser = "INSERT INTO cse360users (username, password, isAdmin, isStudent, isInstructor) VALUES (?, ?, ?, ?, ?)";
	            try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
	                pstmt.setString(1, username);
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

	public boolean isEmailEmpty(String username) {
	    String query = "SELECT email FROM cse360users WHERE username = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, username);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                String email = rs.getString("email");
	                if(email == null || email.trim().isEmpty()) {
	                	return false;
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return true;  // Default return true if no user is found
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

	public void printAllUsers() {
	    String query = "SELECT * FROM cse360users";
	    
	    try (Statement stmt = connection.createStatement();
	         ResultSet rs = stmt.executeQuery(query)) {

	        // Print column headers
	        System.out.println("ID | Username | Password | Email | First Name | Middle Name | Last Name | Preferred First Name | Is Admin | Is Student | Is Instructor");
	        
	        // Iterate over the result set and print each user
	        while (rs.next()) {
	            int id = rs.getInt("id");
	            String username = rs.getString("username");
	            String password = rs.getString("password");
	            String email = rs.getString("email");
	            String firstName = rs.getString("firstName");
	            String middleName = rs.getString("middleName");
	            String lastName = rs.getString("lastName");
	            String preferredFirstName = rs.getString("preferredFirstName");
	            boolean isAdmin = rs.getBoolean("isAdmin");
	            boolean isStudent = rs.getBoolean("isStudent");
	            boolean isInstructor = rs.getBoolean("isInstructor");

	            // Print user info
	            System.out.printf("%d | %s | %s | %s | %s | %s | %s | %s | %b | %b | %b%n",
	                    id, username, password, email, firstName, middleName, lastName, preferredFirstName, isAdmin, isStudent, isInstructor);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
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
	
	
	
	public void updateUserAccountInfo(String username, String email, String firstName, String preferredFirstName, String middleName, String lastName) {
	    String updateQuery = "UPDATE cse360users SET email = ?, firstName = ?, preferredFirstName = ?, middleName = ?, lastName = ? WHERE username = ?";

	    try (PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {
	        pstmt.setString(1, email);
	        pstmt.setString(2, firstName);
	        pstmt.setString(3, preferredFirstName);
	        pstmt.setString(4, middleName);
	        pstmt.setString(5, lastName);
	        pstmt.setString(6, username);

	        int affectedRows = pstmt.executeUpdate();
	        if (affectedRows > 0) {
	            System.out.println("User account info updated successfully.");
	        } else {
	            System.out.println("No user found with the given username.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	
	public boolean doesUserExist(String username) {
	    String query = "SELECT 1 FROM cse360users WHERE username = ? LIMIT 1"; // Check for existence
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {

	        pstmt.setString(1, username);
	        System.out.println("Executing query: " + pstmt);  // Log the query with the parameter
	        
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            System.out.println("User found with username: " + username);
	            return true;  // User exists
	        } else {
	            System.out.println("No user found with username: " + username);
	        }
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;  // If an error occurs or user doesn't exist
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
	
	public String getAllUsers() {
	    String query = "SELECT * FROM cse360users";
	    StringBuilder result = new StringBuilder();
	    
	    try (Statement stmt = connection.createStatement();
	         ResultSet rs = stmt.executeQuery(query)) {

	        // Append column headers
	        result.append("ID | Username | Email | Roles\n");
	        
	        // Iterate over the result set and append the necessary user info to the string builder
	        while (rs.next()) {
	            int id = rs.getInt("id");
	            String username = rs.getString("username");
	            String email = rs.getString("email");

	            // Determine roles based on the boolean values
	            StringBuilder roles = new StringBuilder();
	            if (rs.getBoolean("isAdmin")) {
	                roles.append("Admin");
	            }
	            if (rs.getBoolean("isStudent")) {
	                if (roles.length() > 0) roles.append(", ");
	                roles.append("Student");
	            }
	            if (rs.getBoolean("isInstructor")) {
	                if (roles.length() > 0) roles.append(", ");
	                roles.append("Instructor");
	            }

	            // Append user info to the string builder
	            result.append(String.format("%d | %s | %s | %s%n",
	                    id, username, email, roles.toString()));
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "Error retrieving users.";
	    }

	    // Return the final string
	    return result.toString();
	}

	
	public void backupArticlesToFile(String fileName) throws SQLException, IOException {
		String query = "SELECT * FROM articles";
		try (ResultSet rs = statement.executeQuery(query); FileWriter fileWriter = new FileWriter(fileName)) {

			while (rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String authors = rs.getString("authors");
				String abstractText = rs.getString("abstract");
				String keywords = rs.getString("keywords");
				String body = rs.getString("body");
				String references = rs.getString("references");

				// Writing SQL insert statements into the backup file
				String sqlInsert = String.format(
						"INSERT INTO articles (id, title, authors, abstract, keywords, body, references) "
								+ "VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');\n",
						id, title, authors, abstractText, keywords, body, references);
				fileWriter.write(sqlInsert);
			}
			System.out.println("Backup completed successfully to file: " + fileName);
		} catch (IOException e) {
			System.out.println("Error writing to file: " + e.getMessage());
		}
	}

	/**
	 * Restores articles from a backup file to the database.
	 * 
	 * @param fileName The filename which will be used to perform the restoration.
	 */
	public void restoreArticlesFromFile(String fileName) throws SQLException, IOException {
		// Clear current articles
		String deleteQuery = "DELETE FROM articles";
		statement.executeUpdate(deleteQuery);

		// Load the backup SQL statements from the file
		try (Scanner scanner = new Scanner(new FileReader(fileName))) {
			while (scanner.hasNextLine()) {
				String sql = scanner.nextLine();
				statement.executeUpdate(sql);
			}
			System.out.println("Restore completed successfully from file: " + fileName);
		} catch (IOException e) {
			System.out.println("Error reading from file: " + e.getMessage());
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
