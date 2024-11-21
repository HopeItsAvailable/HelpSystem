package helpSystem;

import java.sql.*;
import java.util.ArrayList;

import Encryption.EncryptionHelper;
import Encryption.EncryptionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import helpSystem.DatabaseHelperUser;

public class DatabaseHelperArticleGroups {
	
	

    // JDBC driver name and database URL
	
	private static DatabaseHelperUser databaseHelper;
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/articleDatabase";

    // Database credentials
    static final String USER = "sa";
    static final String PASS = "";

    private Connection connection = null;
    private Statement statement = null;
    
	private Gson gson = new Gson();
    
	private EncryptionHelper encryptionHelper;

    public DatabaseHelperArticleGroups() throws Exception {
    	encryptionHelper = new EncryptionHelper();
    }

    // Establish connection to the database
    public void connectToDatabase() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER); // Load the JDBC driver
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            statement = connection.createStatement();
            createTables(); // Create the necessary tables if they don't exist
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
        }
    }
    
    

    // Method to create the table for storing article groups
    private void createTables() throws SQLException {
        String articleGroupTable = "CREATE TABLE IF NOT EXISTS cse360ArticleGroups (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "groupName VARCHAR(255) UNIQUE)";
        statement.execute(articleGroupTable);
    }

    // Method to insert a new group into the cse360ArticleGroups table
    public void addArticleGroup(String groupName) throws Exception {
        databaseHelper = new DatabaseHelperUser();
        databaseHelper.connectToDatabase();
        
        // Check if the group already exists
        if (doesGroupExist(groupName)) {
            System.out.println("Group '" + groupName + "' already exists. Skipping insertion.");
            return;
        }

        String query = "INSERT INTO cse360ArticleGroups (groupName) VALUES (?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, groupName);
            pstmt.executeUpdate();
            System.out.println("Added Article Group");

            // Get the username of the first admin
            String adminUsername = databaseHelper.getFirstAdminUsername();
            if (adminUsername != null) {
                System.out.println("Assigning group to first admin: " + adminUsername);

                // Add the new group to the admin's userGroups
                databaseHelper.addUserToGroup(adminUsername, groupName);
                System.out.println("Group '" + groupName + "' assigned to admin: " + adminUsername);
            } else {
                System.out.println("No admin found to assign the group.");
            }
        }
    }
    
    public boolean deleteArticleGroup(String groupName) throws Exception {
    	databaseHelper = new DatabaseHelperUser();
        databaseHelper.connectToDatabase();
    	
    	// Check if the group exists before attempting to delete it
        if (!doesGroupExist(groupName)) {
            System.out.println("Group '" + groupName + "' does not exist. Cannot delete.");
            return false;
        }

        String deleteQuery = "DELETE FROM cse360ArticleGroups WHERE groupName = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteQuery)) {
            pstmt.setString(1, groupName); // Set the group name to the prepared statement
            int rowsAffected = pstmt.executeUpdate(); // Execute the delete statement
            databaseHelper.removeGroupFromAllUsers(groupName);

            if (rowsAffected > 0) {
                System.out.println("Group '" + groupName + "' deleted successfully.");
                return true;
            } else {
                System.out.println("Group deletion failed.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Method to get all groups from the cse360ArticleGroups table
    public ResultSet getAllGroups() throws SQLException {
        String query = "SELECT * FROM cse360ArticleGroups";
        return statement.executeQuery(query);
    }

    // Method to check if a group exists in the cse360ArticleGroups table
    public boolean doesGroupExist(String groupName) throws SQLException {
        String query = "SELECT 1 FROM cse360ArticleGroups WHERE groupName = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, groupName);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Method to get the number of groups in the cse360ArticleGroups table
    public int getNumberOfGroups() throws SQLException {
        String query = "SELECT COUNT(*) AS rowCount FROM cse360ArticleGroups";
        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("rowCount");
            } else {
                return 0;
            }
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
    
    public void backupArticleGroups(String fileName) throws SQLException, IOException {
        closeConnection(); // Close the current database connection

        fileName += ".mv.db"; // Ensure the file has .mv.db extension

        // Define paths for the original and backup database files
        String originalDBPath = Paths.get(System.getProperty("user.home"), "articleDatabase.mv.db").toString();
        String backupFilePath = Paths.get(System.getProperty("user.home"), fileName).toString();

        try {
            Files.copy(Paths.get(originalDBPath), Paths.get(backupFilePath), StandardCopyOption.REPLACE_EXISTING); // Copy the entire database file
            System.out.println("Backup successful!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        connectToDatabase(); // Reconnect to the database
    }
    
    public boolean restoreArticleGroups(String fileName) throws Exception {
    	databaseHelper = new DatabaseHelperUser();
        databaseHelper.connectToDatabase();
    	
    	closeConnection(); // Close the current database connection

        fileName += ".mv.db"; // Ensure the file has .mv.db extension

        // Define paths for the backup and original database files
        String backupFilePath = Paths.get(System.getProperty("user.home"), fileName).toString();
        String originalPath = Paths.get(System.getProperty("user.home"), "articleDatabase.mv.db").toString();

        // Check if the backup file exists
        if (!Files.exists(Paths.get(backupFilePath))) {
            System.out.println("Could not find backup file.");
            return false;
        }

        try {
            // Delete the old database file if it exists
            Files.deleteIfExists(Paths.get(originalPath));

            // Copy the backup file to replace the original database file
            Files.copy(Paths.get(backupFilePath), Paths.get(originalPath), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Restore successful!");
            
            // Reconnect to the database
            connectToDatabase();
            databaseHelper.restoreAdminAccessToAllGroups();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            connectToDatabase(); // Reconnect even on failure
            return false;
        }
    }


}
