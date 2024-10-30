package helpSystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.Base64;

import org.bouncycastle.util.Arrays;

import Encryption.EncryptionHelper;
import Encryption.EncryptionUtils;

class DatabaseHelperArticle {

	// JDBC driver name and database URL 
	static final String JDBC_DRIVER = "org.h2.Driver";   
	static final String DB_URL = "jdbc:h2:~/articleDatabase";  

	//  Database credentials 
	static final String USER = "sa"; 
	static final String PASS = ""; 

	private Connection connection = null;
	private Statement statement = null; 
	//	PreparedStatement pstmt
	
	private EncryptionHelper encryptionHelper;
	
	public DatabaseHelperArticle() throws Exception {
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
		String userTable = "CREATE TABLE IF NOT EXISTS cse360Articles ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "title VARCHAR(255) UNIQUE, "
				+ "author VARCHAR(255), "
				+ "paper_abstract VARCHAR(255),"
				+ "keywords VARCHAR(255),"
				+ "body VARCHAR(255),"
				+ "references VARCHAR(255),"
				+ "level VARCHAR(255))";
		statement.execute(userTable);
	}


	// Check if the database is empty
	public boolean isDatabaseEmpty() throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM cse360Articles";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			return resultSet.getInt("count") == 0;
		}
		return true;
	}

	public void register(char[] title, char[] author, char[] paper_abstract, char[] keywords, char[] body, char[] references, char[] level) throws Exception {
		
		//Convert char to strings
		String titleStr = new String(title);
	    String authorStr = new String(author);
	    String abstractStr = new String(paper_abstract);
	    String keywordsStr = new String(keywords);
	    String bodyStr = new String(body);
	    String referencesStr = new String(references);
	    String levelStr = new String(level);

		
		
		String insertArticle = "INSERT INTO cse360Articles (title, author, paper_abstract, keywords, body, references, level) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertArticle)) {
			pstmt.setString(1, titleStr);
			pstmt.setString(2, authorStr);
			pstmt.setString(3, abstractStr);
			pstmt.setString(4, keywordsStr);
			pstmt.setString(5, bodyStr);
			pstmt.setString(6, referencesStr);
			pstmt.setString(6, levelStr);
			pstmt.executeUpdate();
		}
	}
	
	public boolean doesArticleExist(String title) {
	    String query = "SELECT COUNT(*) FROM cse360Articles WHERE title = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        
	        pstmt.setString(1, title);
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

	public String displayArticles() throws Exception{
		String sql = "SELECT * FROM cse360Articles"; 
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql); 
		
		StringBuilder allArticles = new StringBuilder();

		while(rs.next()) { 
			// Retrieve by column name 
			int id  = rs.getInt("id"); 
			String  title = rs.getString("title"); 
			String author = rs.getString("author");  
			String paper_abstract = rs.getString("paper_abstract");  
			String keywords = rs.getString("keywords");  
			String body = rs.getString("body"); 
			String references = rs.getString("references");  
			String level = rs.getString("level");

			//Get string
			
	        allArticles.append("Article ID: ").append(id)
	                   .append(", Title: ").append(title)
	                   .append(", Author: ").append(author).append("\n")
	                   .append("Abstract: ").append(paper_abstract)
	                   .append(", Keywords: ").append(keywords)
	                   .append(", References: ").append(references)
	                   .append(", Level: ").append(level)
	                   .append("\nBody: ").append(body).append("\n\n");
			
		} 
		
		return allArticles.toString();

	}
	
	public String displayGroupedArticles(String group) throws Exception{
		String sql = "SELECT * FROM cse360Articles WHERE level = ?";
		PreparedStatement stmt = connection.prepareStatement(sql);
	    stmt.setString(1, group);  // Set the level parameter in the query
	    
	    ResultSet rs = stmt.executeQuery(); 
		
		StringBuilder allArticles = new StringBuilder();

		while(rs.next()) { 
			// Retrieve by column name 
			int id  = rs.getInt("id"); 
			String  title = rs.getString("title"); 
			String author = rs.getString("author");  
			String paper_abstract = rs.getString("paper_abstract");  
			String keywords = rs.getString("keywords");  
			String body = rs.getString("body"); 
			String references = rs.getString("references");  
			String level = rs.getString("level");

			//Get string
			
	        allArticles.append("Article ID: ").append(id)
	                   .append(", Title: ").append(title)
	                   .append(", Author: ").append(author).append("\n")
	                   .append("Abstract: ").append(paper_abstract)
	                   .append(", Keywords: ").append(keywords)
	                   .append(", References: ").append(references)
	                   .append(", Level: ").append(level)
	                   .append("\nBody: ").append(body).append("\n\n");
			
		} 
		
		return allArticles.toString();

	}
	
	
	
	public void deleteArticle(String title) throws SQLException {
		if(doesArticleExist(title)) {
			
			String deleteSQL = "DELETE FROM cse360Articles WHERE title = ?";
		    
		    try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)){
		    	
		    	pstmt.setString(1, title);
		   	
		    	pstmt.executeUpdate();
	            
		    }
		}
    }
	
	public void backup(String fileName) throws SQLException {
		
		closeConnection(); // Close the current database connection
		
		fileName += ".mv.db"; //makes it db file

	    // Gets two paths , one for old, and one for new file
	    String originalDBPath = Paths.get(System.getProperty("user.home"), "articleDatabase.mv.db").toString();
	    String backupFilePath = Paths.get(System.getProperty("user.home"), fileName).toString();
		
	    try {
			Files.copy(Paths.get(originalDBPath), Paths.get(backupFilePath), StandardCopyOption.REPLACE_EXISTING); //copies content to new file
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    connectToDatabase();
		
	}
	
	public boolean restore(String fileName) throws SQLException {
		
		closeConnection();
		
		fileName += ".mv.db"; //makes it db file
		
		//finds path of both files
	    String backupFilePath = Paths.get(System.getProperty("user.home"), fileName).toString();
	    String originalPath = Paths.get(System.getProperty("user.home"), "articleDatabase.mv.db").toString();
		
	    //checks if back up file exists
		if (!Files.exists(Paths.get(backupFilePath))) {
			System.out.print("Could not find file");
	        return false;
	    }
		else {
			try {
				Files.deleteIfExists(Paths.get(originalPath)); //deletes old file
	            Files.copy(Paths.get(backupFilePath), Paths.get(originalPath), StandardCopyOption.REPLACE_EXISTING); //copies to old file
				connectToDatabase();
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				connectToDatabase();
				return false;
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

}
