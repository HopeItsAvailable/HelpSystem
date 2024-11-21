package helpSystem;

import java.sql.*;
import java.util.ArrayList;

import Encryption.EncryptionHelper;
import Encryption.EncryptionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
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
}
