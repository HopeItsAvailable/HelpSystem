package helpSystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Encryption.EncryptionHelper;
import Encryption.EncryptionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
	private static DatabaseHelperArticle databaseHelper1;
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
    
    public void updateGroupLeader(String groupName, String newLeader) throws SQLException {
        // Check if the group exists
        if (!doesGroupExist(groupName)) {
            System.out.println("Group '" + groupName + "' does not exist. Cannot update leader.");
            return;
        }

        String updateQuery = "UPDATE cse360ArticleGroups SET leader = ? WHERE groupName = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {
            pstmt.setString(1, newLeader); 
            pstmt.setString(2, groupName); 
            pstmt.executeUpdate(); 
            
        }
            
    }
    
    public boolean doesGroupHaveLeader(String groupName) throws SQLException {
        String query = "SELECT leader FROM cse360ArticleGroups WHERE groupName = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, groupName); // Specify the group name
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String leader = rs.getString("leader");
                    if (leader != null && !leader.isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public void updateAdmin(String groupName, String admin) throws SQLException {
        // Check if the group exists
        if (!doesGroupExist(groupName)) {
            return;
        }

        String query = "UPDATE cse360ArticleGroups SET admin = ? WHERE groupName = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, admin);
            pstmt.setString(2, groupName); 
            pstmt.executeUpdate();

        }
    }
    
    public boolean doesGroupHaveAdmin(String groupName) throws SQLException {
        // Check if the group exists
        if (!doesGroupExist(groupName)) {
            return false;
        }

        String query = "SELECT admin FROM cse360ArticleGroups WHERE groupName = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, groupName); 
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String admin = rs.getString("admin"); 
                    return admin != null && !admin.isEmpty();
                }
            }
        }
        return false; // Return false if no admin is assigned
    }
    

    // Method to create the table for storing article groups
    private void createTables() throws SQLException {
        String articleGroupTable = "CREATE TABLE IF NOT EXISTS cse360ArticleGroups (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "leader VARCHAR(255), " + 
                "groupName VARCHAR(255) NOT NULL UNIQUE, " + 
                "admin VARCHAR(255)" +
                ")";
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
        
        databaseHelper1 = new DatabaseHelperArticle();
        databaseHelper1.connectToDatabase();
    	
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
            databaseHelper1.setDisplayFalseForGroup(groupName);

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
    
    public ArrayList<String> getUserGroupsWhereLeaderOrAdmin(String userName) throws SQLException {
        ArrayList<String> groups = new ArrayList<>();
        String query = "SELECT groupName FROM cse360ArticleGroups WHERE leader = ? OR admin = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, userName);
            pstmt.setString(2, userName);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    groups.add(rs.getString("groupName"));
                }
            }
        }
        return groups;
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
    
    public void backupArticleGroupsToFile(String fileName, String groupName) throws SQLException, IOException {
        String query = "SELECT * FROM cse360ArticleGroups WHERE groupName = ?";  // Filter by groupName
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, groupName);  // Set the group name dynamically
            
            try (ResultSet rs = pstmt.executeQuery(); FileWriter fileWriter = new FileWriter(fileName)) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String group = rs.getString("groupName");

                    // Writing SQL insert statements into the backup file
                    String sqlInsert = String.format(
                            "INSERT INTO cse360ArticleGroups (id, groupName) "
                                    + "VALUES (%d, '%s');\n", id, group);
                    fileWriter.write(sqlInsert);
                }
                System.out.println("Backup completed successfully to file: " + fileName);
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
    
    public void restoreArticleGroupsFromFile(String fileName) throws Exception {
        // Step 1: Ensure database connection is established
        databaseHelper = new DatabaseHelperUser();
        databaseHelper.connectToDatabase();

        System.out.println("Connecting to database...");

        File file = new File(fileName);
        if (!file.exists()) {
            System.err.println("Backup file not found: " + fileName);
            return;
        }
        System.out.println("File found: " + fileName);

        try (Scanner scanner = new Scanner(new FileReader(file))) {
            while (scanner.hasNextLine()) {
                String sql = scanner.nextLine().trim();

                // Skip empty lines or comments
                if (sql.isEmpty() || sql.startsWith("--")) {
                    System.out.println("Skipping line: " + sql);
                    continue;
                }

                System.out.println("Executing SQL: " + sql);

                // Check if the SQL statement is for cse360ArticleGroups
                if (sql.toLowerCase().contains("insert into cse360articlegroups")) {
                    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                        pstmt.executeUpdate();
                        String groupName = extractGroupNameFromSQL(sql);
                        System.out.println("Restored group: " + (groupName != null ? groupName : "Unknown"));
                    } catch (SQLException e) {
                        System.err.println("Error executing SQL: " + sql);
                        e.printStackTrace();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Backup file not found: " + fileName);
            throw new Exception("Failed to restore groups. Backup file not found.", e);
        } catch (IOException e) {
            System.err.println("Error reading from file: " + fileName);
            throw new Exception("Failed to restore groups. Error reading file.", e);
        }
    }

       
    	private String extractGroupNameFromSQL(String sql) {
    	    // Check if the SQL string contains "VALUES"
    	    if (!sql.toUpperCase().contains("VALUES")) {
    	        return null;
    	    }

    	    try {
    	        // Split the SQL by "VALUES"
    	        String[] parts = sql.split("VALUES");

    	        // Get the values part (after "VALUES")
    	        String valuesPart = parts[1].trim();

    	        // Remove enclosing parentheses
    	        valuesPart = valuesPart.substring(1, valuesPart.length() - 1);

    	        // Split the values by comma (assuming the first value is the groupName)
    	        String[] values = valuesPart.split(",");

    	        // Extract the first value, remove surrounding quotes, and return it
    	        return values[0].trim().replaceAll("'", "");
    	    } catch (Exception e) {
    	        System.err.println("Error extracting groupName from SQL: " + sql);
    	        e.printStackTrace();
    	        return null;
    	    }
    	}

       /* // Step 1: Load the backup SQL statements from the file
    	
    	databaseHelper = new DatabaseHelperUser();
        databaseHelper.connectToDatabase();
        
        try (Scanner scanner = new Scanner(new FileReader(fileName))) {
            while (scanner.hasNextLine()) {
                String sql = scanner.nextLine().trim();  // Trim whitespace to avoid empty or malformed lines

                // Skip empty lines or comments
                if (sql.isEmpty() || sql.startsWith("--")) {
                    continue;
                }

                // Check if the SQL statement is for inserting into cse360ArticleGroups
                if (sql.toLowerCase().contains("insert into cse360articlegroups")) {
                    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                        pstmt.executeUpdate(); // Execute the insert statement to restore the group
                    }
                }
            }

            // Optional step: Restore articles associated with the groups
            //restoreArticlesForGroups();
            databaseHelper.restoreAdminAccessToAllGroups();
            System.out.println("Restore completed successfully from file: " + fileName);
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
        */
    

    

  /*
    public void backupGroups(String fileName, boolean isAdmin, String username) throws SQLException, IOException {
        closeConnection(); // Close the current database connection

        fileName += (isAdmin ? "_Admin" : "_" + username + "_Instructor") + ".json"; // Role-based file naming

        String query;
        if (isAdmin) {
            // Admins can back up all groups
            query = "SELECT groupName FROM cse360ArticleGroups";
        } else {
            // Instructors back up only groups they are part of
            query = "SELECT groupName FROM cse360ArticleGroups "
                  + "WHERE groupName IN (SELECT groupName FROM cse360users WHERE username = ?)";
        }

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            if (!isAdmin) {
                pstmt.setString(1, username); // Filter by username for instructors
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                List<String> groups = new ArrayList<>();
                while (rs.next()) {
                    groups.add(rs.getString("groupName"));
                }
                String json = gson.toJson(groups);
                Files.writeString(Paths.get(System.getProperty("user.home"), fileName), json);
                System.out.println("Backup successful for " + (isAdmin ? "Admin" : "Instructor: " + username));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectToDatabase(); // Reconnect to the database
        }
    }
    
    public boolean restoreGroups(String fileName, boolean isAdmin, String username) throws SQLException, IOException {
        closeConnection(); // Close the current database connection

        fileName += (isAdmin ? "_Admin" : "_" + username + "_Instructor") + ".json"; // Role-based file naming

        String backupFilePath = Paths.get(System.getProperty("user.home"), fileName).toString();
        if (!Files.exists(Paths.get(backupFilePath))) {
            System.out.println("Backup file not found for " + (isAdmin ? "Admin" : "Instructor: " + username));
            return false;
        }

        String json = Files.readString(Paths.get(backupFilePath));
        List<String> groups = gson.fromJson(json, new TypeToken<List<String>>() {}.getType());

        String insertQuery = "INSERT INTO cse360ArticleGroups (groupName) VALUES (?) ON DUPLICATE KEY UPDATE groupName = groupName";

        try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
            for (String group : groups) {
                pstmt.setString(1, group);
                pstmt.executeUpdate();
            }

            // If the user is an instructor, ensure they are only restoring their groups
            if (!isAdmin) {
                for (String group : groups) {
                    // Add restored group to the instructor's userGroups field
                    String updateUserGroups = "UPDATE cse360users SET userGroups = CONCAT(userGroups, ',', ?) WHERE username = ?";
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateUserGroups)) {
                        updateStmt.setString(1, group);
                        updateStmt.setString(2, username);
                        updateStmt.executeUpdate();
                    }
                }
            }

            System.out.println("Restore successful for " + (isAdmin ? "Admin" : "Instructor: " + username));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            connectToDatabase(); // Reconnect to the database
        }
    }
    
*/

}
