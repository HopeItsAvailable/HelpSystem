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

public class DatabaseHelperMes {
	
	// JDBC driver name and database URL
	
		private static DatabaseHelperUser databaseHelper;
		private static DatabaseHelperArticle databaseHelper1;
	    static final String JDBC_DRIVER = "org.h2.Driver";
	    static final String DB_URL = "jdbc:h2:~/mesDatabase";

	    // Database credentials
	    static final String USER = "sa";
	    static final String PASS = "";
	    

	    private Connection connection = null;
	    private Statement statement = null;
	    
		private EncryptionHelper encryptionHelper;
		
		public DatabaseHelperMes() throws Exception {
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
	    	String articleGroupTable = "CREATE TABLE IF NOT EXISTS Messages (" +
	    		    "id INT AUTO_INCREMENT PRIMARY KEY, " +
	    		    "userName VARCHAR(255), " +
	    		    "message VARCHAR(255)" +
	    		    ")";
	        statement.execute(articleGroupTable);
	    }
	    
	    // Insert a message into the table
	    public void addMessage(String userName, String message) throws SQLException {
	        String sql = "INSERT INTO Messages (userName, message) VALUES (?, ?)";
	        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	            preparedStatement.setString(1, userName);
	            preparedStatement.setString(2, message);
	            preparedStatement.executeUpdate();
	        }
	    }

	    public String getMessages() throws SQLException {
	        StringBuilder messages = new StringBuilder();
	        String sql = "SELECT userName, message FROM Messages";
	        try (ResultSet resultSet = statement.executeQuery(sql)) {
	            while (resultSet.next()) {
	                String userName = resultSet.getString("userName");
	                String message = resultSet.getString("message");
	                messages.append(userName).append(": ").append(message).append("\n\n");
	            }
	        }
	        return messages.toString().trim(); 
	    }

}
