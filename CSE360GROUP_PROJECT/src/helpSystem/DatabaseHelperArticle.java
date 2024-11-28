package helpSystem;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;


import org.bouncycastle.util.Arrays;

import helpSystem.DatabaseHelperUser;
import Encryption.EncryptionHelper;
import Encryption.EncryptionUtils;

public class DatabaseHelperArticle {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "org.h2.Driver";
	static final String DB_URL = "jdbc:h2:~/articleDatabase";

	// Database credentials
	static final String USER = "sa";
	static final String PASS = "";

	private Connection connection = null;
	private Statement statement = null;
	// PreparedStatement pstmt

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
			createTables(); // Create the necessary tables if they don't exist
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver not found: " + e.getMessage());
		}
	}

	private void createTables() throws SQLException {
		String userTable = "CREATE TABLE IF NOT EXISTS cse360Articles (" + "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "title VARCHAR(255) UNIQUE, " + "author VARCHAR(255), " + "paper_abstract VARCHAR(255),"
				+ "keywords VARCHAR(255)," + "body VARCHAR(255)," + "references VARCHAR(255)," + "level VARCHAR(255),"
				+ "articleGroup VARCHAR(255))";
		statement.execute(userTable);
	}
	
	public int getNumberOfArticles() throws SQLException {
	    String query = "SELECT COUNT(*) AS rowCount FROM cse360Articles";
	    try (PreparedStatement pstmt = connection.prepareStatement(query);
	         ResultSet rs = pstmt.executeQuery()) {
	        if (rs.next()) {
	            return rs.getInt("rowCount");
	        } else {
	            return 0;
	        }
	    }
	}
	
	//METHODS TO MAKE:
	//print all (unencrypted and encrypted)
	//print by group (unencrypted and encrypted)
	//get number of rows in a table
	
	
	
	public ResultSet searchArticles(String username, String searchQuery, String groupFilter, boolean viewAll) throws Exception {
	    DatabaseHelperUser userHelper = new DatabaseHelperUser();
	    userHelper.connectToDatabase();
	    
	    ArrayList<String> userGroups = userHelper.getUserGroups(username);
	    String query = "SELECT * FROM cse360Articles WHERE (title LIKE ? OR author LIKE ? OR paper_abstract LIKE ?)";
	    
	    if (viewAll == false) {
	        if (groupFilter != null && !groupFilter.isEmpty()) {
	            if (userGroups.contains(groupFilter)) {
	                query += " AND articleGroup = ?";
	            } else {
	                System.out.println("You do not have access to this group.");
	                return null;
	            }
	        } else {
	            query += " AND articleGroup IN (" + String.join(",", userGroups.stream().map(g -> "?").toList()) + ")";
	        }
	    }

	    // Prepare statement and set parameters
	    PreparedStatement pstmt = connection.prepareStatement(query);
	    pstmt.setString(1, "%" + searchQuery + "%");
	    pstmt.setString(2, "%" + searchQuery + "%");
	    pstmt.setString(3, "%" + searchQuery + "%");

	    int parameterIndex = 4;
	    if (viewAll == false) {
	        if (groupFilter != null && !groupFilter.isEmpty()) {
	            pstmt.setString(4, groupFilter);
	        } else {
	            for (String group : userGroups) {
	                pstmt.setString(parameterIndex++, group);
	            }
	        }
	    }

	    // Execute query and return the ResultSet
	    return pstmt.executeQuery();
	}

	public boolean isSpecialGroupByID(int ID) throws SQLException {
	    String query = "SELECT articleGroup FROM cse360Articles WHERE id = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setInt(1, ID); // Set the article ID
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                String group = rs.getString("articleGroup"); // Get the article group
	                return !"default".equalsIgnoreCase(group); // Return true if the group is not "default"
	            } else {
	                throw new IllegalArgumentException("Article with ID " + ID + " does not exist.");
	            }
	        }
	    }
	}
	


	public String getArticleGroupByID(int ID) throws SQLException {
	    String query = "SELECT articleGroup FROM cse360Articles WHERE id = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setInt(1, ID); // Set the ID parameter
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getString("articleGroup"); // Return the article group if the record exists
	            } else {
	                return null; // Return null if no record is found
	            }
	        }
	    }
	}
	
	public String getArticleGroupByTitle(String title) throws SQLException {
	    String query = "SELECT articleGroup FROM cse360Articles WHERE title = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, title); // Set the ID parameter
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getString("articleGroup"); // Return the article group if the record exists
	            } else {
	                return null; // Return null if no record is found
	            }
	        }
	    }
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

	/*public void register(char[] title, char[] author, char[] paper_abstract, char[] keywords, char[] body,
			char[] references, char[] level, char[] group) throws Exception {

		// Convert char to strings
		String titleStr = new String(title);
		String authorStr = new String(author);
		String abstractStr = new String(paper_abstract);
		String keywordsStr = new String(keywords);
		String bodyStr = new String(body);
		String referencesStr = new String(references);
		String levelStr = new String(level);
		String articleGroup = new String(group);

		String insertArticle = "INSERT INTO cse360Articles (title, author, paper_abstract, keywords, body, references, level, articleGroup) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertArticle)) {
			pstmt.setString(1, titleStr);
			pstmt.setString(2, authorStr);
			pstmt.setString(3, abstractStr);
			pstmt.setString(4, keywordsStr);
			pstmt.setString(5, bodyStr);
			pstmt.setString(6, referencesStr);
			pstmt.setString(7, levelStr);
			pstmt.setString(8, articleGroup);
			pstmt.executeUpdate();
			
		}
	}*/
	//encryps when article is created
	public void register(char[] title, char[] author, char[] paper_abstract, char[] keywords, char[] body,
            char[] references, char[] level, char[] group) throws Exception 
    {

		// Convert char arrays to Strings
		String titleStr = new String(title);
		String authorStr = new String(author);
		String abstractStr = new String(paper_abstract);
		String keywordsStr = new String(keywords);
		String bodyStr = new String(body);
		String referencesStr = new String(references);
		String levelStr = new String(level);
		String articleGroup = new String(group);

		// Initialize encryption helper
		EncryptionHelper encryptionHelper = new EncryptionHelper();

		// Use a fixed initialization vector for simplicity (not recommended for real-world applications)
		byte[] iv = new byte[16]; // 16 bytes for AES (128-bit block size)

		// Encrypt only the body
		byte[] encryptedBody = encryptionHelper.encrypt(bodyStr.getBytes(), iv);

		String insertArticle = "INSERT INTO cse360Articles (title, author, paper_abstract, keywords, body, references, level, articleGroup) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertArticle)) 
		{
			pstmt.setString(1, titleStr);
			pstmt.setString(2, authorStr);
			pstmt.setString(3, abstractStr);
			pstmt.setString(4, keywordsStr);
			//encrypted body
			pstmt.setBytes(5, encryptedBody);
			pstmt.setString(6, referencesStr);
			pstmt.setString(7, levelStr);
			pstmt.setString(8, articleGroup);
			pstmt.executeUpdate();
		}
}

	public String decryptBody(int articleId) throws Exception 
	{
	    String selectQuery = "SELECT body FROM cse360Articles WHERE id = ?";
	    String decryptedBody = null;

	    // Initialize encryption helper
	    EncryptionHelper encryptionHelper = new EncryptionHelper();

	    // Use the same initialization vector (IV) that was used during encryption
	    byte[] iv = new byte[16]; // 16 bytes for AES (128-bit block size)

	    try (PreparedStatement pstmt = connection.prepareStatement(selectQuery)) 
	    {
	        pstmt.setInt(1, articleId);
	        try (ResultSet rs = pstmt.executeQuery()) 
	        {
	            if (rs.next()) 
	            {
	                byte[] encryptedBody = rs.getBytes("body");
	                if (encryptedBody != null) 
	                {
	                    // Decrypt the body
	                    byte[] decryptedBytes = encryptionHelper.decrypt(encryptedBody, iv);
	                    decryptedBody = new String(decryptedBytes);
	                }
	            }
	        }
	    }

	    return decryptedBody;
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

	/*public String displayArticles() throws Exception {
		String sql = "SELECT * FROM cse360Articles";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		StringBuilder allArticles = new StringBuilder();

		while (rs.next()) {
			// Retrieve by column name
			int id = rs.getInt("id");
			String title = rs.getString("title");
			String author = rs.getString("author");
			String paper_abstract = rs.getString("paper_abstract");
			String keywords = rs.getString("keywords");
			String body = rs.getString("body");
			String references = rs.getString("references");
			String level = rs.getString("level");

			// Get string

			allArticles.append("Article ID: ").append(id).append(", Title: ").append(title).append(", Author: ")
					.append(author).append("\n").append("Abstract: ").append(paper_abstract).append(", Keywords: ")
					.append(keywords).append(", References: ").append(references).append(", Level: ").append(level)
					.append("\nBody: ").append(body).append("\n\n");

		}

		return allArticles.toString();

	}*/
	
	public ObservableList<Article> displayArticles() throws SQLException {
	    String sql = "SELECT * FROM cse360Articles";
	    Statement stmt = connection.createStatement();
	    ResultSet rs = stmt.executeQuery(sql);

	    ObservableList<Article> articles = FXCollections.observableArrayList();

	    while (rs.next()) {
	        int id = rs.getInt("id");
	        String title = rs.getString("title");
	        String author = rs.getString("author");
	        String paperAbstract = rs.getString("paper_abstract");
	        String keywords = rs.getString("keywords");
	        String body = rs.getString("body");
	        String references = rs.getString("references");
	        String level = rs.getString("level");
	        String group = rs.getString("articleGroup");

	        articles.add(new Article(id, title, author, paperAbstract, keywords, body, references, level, group));
	    }

	    return articles;
	}

	public String displayGroupedArticles(String group) throws Exception {
		String sql = "SELECT * FROM cse360Articles WHERE level = ?";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, group); // Set the level parameter in the query

		ResultSet rs = stmt.executeQuery();

		StringBuilder allArticles = new StringBuilder();

		while (rs.next()) {
			// Retrieve by column name
			int id = rs.getInt("id");
			String title = rs.getString("title");
			String author = rs.getString("author");
			String paper_abstract = rs.getString("paper_abstract");
			String keywords = rs.getString("keywords");
			String body = rs.getString("body");
			String references = rs.getString("references");
			String level = rs.getString("level");

			// Get string

			allArticles.append("Article ID: ").append(id).append(", Title: ").append(title).append(", Author: ")
					.append(author).append("\n").append("Abstract: ").append(paper_abstract).append(", Keywords: ")
					.append(keywords).append(", References: ").append(references).append(", Level: ").append(level)
					.append("\nBody: ").append(body).append("\n\n");

		}

		return allArticles.toString();

	}

	public void deleteArticle(String title) throws SQLException {
		if (doesArticleExist(title)) {

			String deleteSQL = "DELETE FROM cse360Articles WHERE title = ?";

			try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {

				pstmt.setString(1, title);

				pstmt.executeUpdate();

			}
		}
	}

	public void backup(String fileName) throws SQLException {

		closeConnection(); // Close the current database connection

		fileName += ".mv.db"; // makes it db file

		// Gets two paths , one for old, and one for new file
		String originalDBPath = Paths.get(System.getProperty("user.home"), "articleDatabase.mv.db").toString();
		String backupFilePath = Paths.get(System.getProperty("user.home"), fileName).toString();

		try {
			Files.copy(Paths.get(originalDBPath), Paths.get(backupFilePath), StandardCopyOption.REPLACE_EXISTING); // copies
																													// content
																													// to
																													// new
																													// file
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connectToDatabase();

	}

	public boolean restore(String fileName) throws SQLException {

		closeConnection();

		fileName += ".mv.db"; // makes it db file

		// finds path of both files
		String backupFilePath = Paths.get(System.getProperty("user.home"), fileName).toString();
		String originalPath = Paths.get(System.getProperty("user.home"), "articleDatabase.mv.db").toString();

		// checks if back up file exists
		if (!Files.exists(Paths.get(backupFilePath))) {
			System.out.print("Could not find file");
			return false;
		} else {
			try {
				Files.deleteIfExists(Paths.get(originalPath)); // deletes old file
				Files.copy(Paths.get(backupFilePath), Paths.get(originalPath), StandardCopyOption.REPLACE_EXISTING); // copies
																														// to
																														// old
																														// file
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

//	public void restoreArticlesFromFile(String fileName) throws SQLException, IOException {
//		// Clear current articles
//		String deleteQuery = "DELETE FROM articles";
//		statement.executeUpdate(deleteQuery);
//
//		// Load the backup SQL statements from the file
//		try (Scanner scanner = new Scanner(new FileReader(fileName))) {
//			while (scanner.hasNextLine()) {
//				String sql = scanner.nextLine();
//				statement.executeUpdate(sql);
//			}
//			System.out.println("Restore completed successfully from file: " + fileName);
//		} catch (IOException e) {
//			System.out.println("Error reading from file: " + e.getMessage());
//		}
//	}

	public boolean mergeData(String backupFileName) throws Exception {

		backupFileName += ".mv.db";

		String backupFilePath = Paths.get(System.getProperty("user.home"), backupFileName).toString();
		String originalPath = Paths.get(System.getProperty("user.home"), "articleDatabase.mv.db").toString();

		// Check if the backup file exists
		if (!Files.exists(Paths.get(backupFilePath))) {
			System.out.println("Backup file not found.");
			return false;
		}

		// JDBC URL for the backup database
		String backupDbUrl = "jdbc:h2:" + backupFilePath.replace(".mv.db", "");

		try (Connection backupConn = DriverManager.getConnection(backupDbUrl, USER, PASS)) {
			// Fetch all articles from the backup database
			String queryBackup = "SELECT * FROM cse360Articles";
			try (Statement stmtBackup = backupConn.createStatement();
					ResultSet rsBackup = stmtBackup.executeQuery(queryBackup)) {

				while (rsBackup.next()) {
					// Get all fields from the backup article
					String title = rsBackup.getString("title");
					String author = rsBackup.getString("author");
					String paperAbstract = rsBackup.getString("paper_abstract");
					String keywords = rsBackup.getString("keywords");
					String body = rsBackup.getString("body");
					String references = rsBackup.getString("references");
					String level = rsBackup.getString("level");
					String group = rsBackup.getString("group");

					// Check if the article with the same title already exists in the original
					// database
					if (!doesArticleExist(title)) {
						// Insert the record if it doesn't exist
						String insertQuery = "INSERT INTO cse360Articles (title, author, paper_abstract, keywords, body, references, level) VALUES (?, ?, ?, ?, ?, ?, ?)";
						try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
							register(title.toCharArray(), author.toCharArray(), paperAbstract.toCharArray(),
									keywords.toCharArray(), body.toCharArray(), references.toCharArray(),
									level.toCharArray(), group.toCharArray());
						}
					}
				}
			}

			System.out.println("Merge done");
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateArticleBody(String title, String newBody) throws SQLException {
		// Check if the article exists before attempting to update
		if (doesArticleExist(title)) {
			// SQL statement to update only the body field based on the title
			String updateSQL = "UPDATE cse360Articles SET body = ? WHERE title = ?";

			try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
				// Set the new value for the body field
				pstmt.setString(1, newBody);
				pstmt.setString(2, title); // Where clause to match the title

				int rowsAffected = pstmt.executeUpdate();

				return true;
			}
		} else {
			System.out.println("Article with title '" + title + "' does not exist.");
			return false;
		}
	}
	
	public String getArticleByIdAndUserName(int id, String userName) throws Exception {
	    // Initialize the DatabaseHelperUser to fetch user groups
	    DatabaseHelperUser userHelper = new DatabaseHelperUser();
	    userHelper.connectToDatabase();

	    // Fetch the user's groups
	    ArrayList<String> userGroups = userHelper.getUserGroups(userName);

	    // SQL query to get the article details
	    String query = "SELECT * FROM cse360Articles WHERE id = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setInt(1, id);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                // Get the article's group
	                String articleGroup = rs.getString("articleGroup");

	                // Check if the user has access to this group
	                if (userGroups.contains(articleGroup)) {
	                    // Fetch article details
	                    String title = rs.getString("title");
	                    String author = rs.getString("author");
	                    String paperAbstract = rs.getString("paper_abstract");
	                    String keywords = rs.getString("keywords");
	                    String references = rs.getString("references");
	                    String level = rs.getString("level");
	                    String group = rs.getString("articleGroup");

	                    // Decrypt the body
	                    String decryptedBody = decryptBody(id);

	                    // Construct the article string
	                    return "Article ID: " + id + "\n" +
	                            "Title: " + title + "\n" +
	                            "Author: " + author + "\n" +
	                            "Abstract: " + paperAbstract + "\n" +
	                            "Keywords: " + keywords + "\n" +
	                            "Body: " + decryptedBody + "\n" +
	                            "References: " + references + "\n" +
	                            "Level: " + level + "\n" +
	                            "Group: " + group;
	                } else {
	                    throw new IllegalAccessException("Access denied: You do not have access to this article.");
	                }
	            } else {
	                throw new IllegalArgumentException("Article with ID " + id + " does not exist.");
	            }
	        }
	    }
	}
	
	public Boolean checkArticleByIdAndUserName(int id, String userName) throws Exception {
	    // Initialize the DatabaseHelperUser to fetch user groups
	    DatabaseHelperUser userHelper = new DatabaseHelperUser();
	    userHelper.connectToDatabase();

	    // Fetch the user's groups
	    ArrayList<String> userGroups = userHelper.getUserGroups(userName);

	    // SQL query to get the article details
	    String query = "SELECT * FROM cse360Articles WHERE id = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setInt(1, id);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                // Get the article's group
	                String articleGroup = rs.getString("articleGroup");

	                // Check if the user has access to this group
	                if (userGroups.contains(articleGroup)) {

	                    return true;
	                } else {
	                	return false;
	                }
	            } else {
	            	return false;
	            }
	        }
	    }
	}

	public void closeConnection() {
		try {
			if (statement != null)
				statement.close();
		} catch (SQLException se2) {
			se2.printStackTrace();
		}
		try {
			if (connection != null)
				connection.close();
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

}
