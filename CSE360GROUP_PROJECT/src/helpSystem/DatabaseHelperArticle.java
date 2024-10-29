package helpSystem;

import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.*;
import java.io.*;

import org.bouncycastle.util.Arrays;

import Encryption.EncryptionHelper;
import Encryption.EncryptionUtils;

class DatabaseHelperArticle {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "org.h2.Driver";
	static final String DB_URL = "jdbc:h2:~/firstDatabase";
	static final String IV = "Apple2182139";

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
	/**
	 * Connects to the database and creates table of articles.
	 */
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

	/**
	 * Creates the article table for the database if one is not yet created.
	 */
	private void createTables() throws SQLException {
		String articleTable = "CREATE TABLE IF NOT EXISTS articles (" + "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "title TEXT, " + "authors TEXT, " + "abstract TEXT, " + "keywords TEXT, " + "body TEXT, "
				+ "references TEXT)";
		statement.execute(articleTable);
	}

	/**
	 * Checks if the database is empty by counting the number of rows in the article
	 * table.
	 */
	public boolean isDatabaseEmpty() throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM cse360users";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) { // Check if the database is empty using sql command
			return resultSet.getInt("count") == 0;
		}
		return true;
	}

	/**
	 * Lists all articles from the database, decrypting only the title and author,
	 * while keeping everything else encrypted.
	 */
	public void listArticles() throws Exception {
		String query = "SELECT id, title, authors FROM articles";
		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
			// Check if the result set is empty
			if (!rs.isBeforeFirst()) {
				System.out.println("No articles found.");
				return; // Exit the method if no articles are found
			}

			System.out.println("List of Articles:");
			int index = 1;
			while (rs.next()) {
				int id = rs.getInt("id");
				String encryptedTitle = rs.getString("title"); // Retrieve encrypted title
				String encryptedAuthors = rs.getString("authors"); // Retrieve encrypted authors

				// Decrypt title and authors
				char[] decryptedTitle = EncryptionUtils
						.toCharArray(encryptionHelper.decrypt(Base64.getDecoder().decode(encryptedTitle),
								EncryptionUtils.getInitializationVector(IV.toCharArray())));

				char[] decryptedAuthors = EncryptionUtils
						.toCharArray(encryptionHelper.decrypt(Base64.getDecoder().decode(encryptedAuthors),
								EncryptionUtils.getInitializationVector(IV.toCharArray())));

				// Print the article details with decrypted values
				System.out.println(index + ". Sequence Number: " + id + " Title: " + new String(decryptedTitle)
						+ ", Authors: " + new String(decryptedAuthors));
				index++;
				// makes array of decrypted data blank
				clearArray(decryptedTitle);
				clearArray(decryptedAuthors);

			}
		}

	}

	public void addArticle(String title, String authors, String abstractText, String keywords, String body,
			String references) throws SQLException, Exception {

		// encrypt each field to place into memory

		String encryptedTitle = Base64.getEncoder().encodeToString(
				encryptionHelper.encrypt(title.getBytes(), EncryptionUtils.getInitializationVector(IV.toCharArray())));

		String encryptedAuthors = Base64.getEncoder().encodeToString(encryptionHelper.encrypt(authors.getBytes(),
				EncryptionUtils.getInitializationVector(IV.toCharArray())));

		String encryptedAbstract = Base64.getEncoder().encodeToString(encryptionHelper.encrypt(abstractText.getBytes(),
				EncryptionUtils.getInitializationVector(IV.toCharArray())));

		String encryptedKeywords = Base64.getEncoder().encodeToString(encryptionHelper.encrypt(keywords.getBytes(),
				EncryptionUtils.getInitializationVector(IV.toCharArray())));

		String encryptedBody = Base64.getEncoder().encodeToString(
				encryptionHelper.encrypt(body.getBytes(), EncryptionUtils.getInitializationVector(IV.toCharArray())));

		String encryptedReferences = Base64.getEncoder().encodeToString(encryptionHelper.encrypt(references.getBytes(),
				EncryptionUtils.getInitializationVector(IV.toCharArray())));

		// Insert the encrypted article into the database
		String insertArticle = "INSERT INTO articles (title, authors, abstract, keywords, body, references) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertArticle)) {
			pstmt.setString(1, encryptedTitle);
			pstmt.setString(2, encryptedAuthors);
			pstmt.setString(3, encryptedAbstract);
			pstmt.setString(4, encryptedKeywords);
			pstmt.setString(5, encryptedBody);
			pstmt.setString(6, encryptedReferences);
			pstmt.executeUpdate();
		}
	}

	/**
	 * Deletes an article from the database by its sequence number which is like an
	 * ID.
	 * 
	 * @Param articleId is the sequence number which will be deleted.
	 */
	public void deleteArticle(int articleId) throws SQLException {
		String deleteQuery = "DELETE FROM articles WHERE id = ?"; // sql command
		try (PreparedStatement pstmt = connection.prepareStatement(deleteQuery)) {
			pstmt.setInt(1, articleId);
			int rowsAffected = pstmt.executeUpdate();
			if (rowsAffected > 0) { // if true
				System.out.println("Article deleted successfully.");
			} else {
				System.out.println("Article not found.");
			}
		}
	}

	/**
	 * Backs up the articles from the database to a file. If there is no file by
	 * that name in the working directory, one is created using the FileWriter.
	 * 
	 * @param fileName The filename which will be used to perform the backup.
	 */
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

	/**
	 * Displays an article by its sequence number.
	 * 
	 * @param articleId The sequence number (ID) of the article to display.
	 */
	public void displayArticleById(int articleId) throws Exception {
		String query = "SELECT id, title, authors, abstract, keywords, body, references FROM articles WHERE id = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, articleId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					// Decrypt and display the article's details
					String encryptedTitle = rs.getString("title");
					String encryptedAuthors = rs.getString("authors");
					String encryptedAbstract = rs.getString("abstract");
					String encryptedKeywords = rs.getString("keywords");
					String encryptedBody = rs.getString("body");
					String encryptedReferences = rs.getString("references");

					// Decrypt each field
					char[] decryptedTitle = EncryptionUtils
							.toCharArray(encryptionHelper.decrypt(Base64.getDecoder().decode(encryptedTitle),
									EncryptionUtils.getInitializationVector(IV.toCharArray())));
					char[] decryptedAuthors = EncryptionUtils
							.toCharArray(encryptionHelper.decrypt(Base64.getDecoder().decode(encryptedAuthors),
									EncryptionUtils.getInitializationVector(IV.toCharArray())));
					char[] decryptedAbstract = EncryptionUtils
							.toCharArray(encryptionHelper.decrypt(Base64.getDecoder().decode(encryptedAbstract),
									EncryptionUtils.getInitializationVector(IV.toCharArray())));
					char[] decryptedKeywords = EncryptionUtils
							.toCharArray(encryptionHelper.decrypt(Base64.getDecoder().decode(encryptedKeywords),
									EncryptionUtils.getInitializationVector(IV.toCharArray())));
					char[] decryptedBody = EncryptionUtils
							.toCharArray(encryptionHelper.decrypt(Base64.getDecoder().decode(encryptedBody),
									EncryptionUtils.getInitializationVector(IV.toCharArray())));
					char[] decryptedReferences = EncryptionUtils
							.toCharArray(encryptionHelper.decrypt(Base64.getDecoder().decode(encryptedReferences),
									EncryptionUtils.getInitializationVector(IV.toCharArray())));

					// Display the article details
					System.out.println("Article ID: " + articleId);
					System.out.println("Title: " + new String(decryptedTitle));
					System.out.println("Authors: " + new String(decryptedAuthors));
					System.out.println("Abstract: " + new String(decryptedAbstract));
					System.out.println("Keywords: " + new String(decryptedKeywords));
					System.out.println("Body: " + new String(decryptedBody));
					System.out.println("References: " + new String(decryptedReferences));

					clearArray(decryptedTitle);
					clearArray(decryptedAuthors);
					clearArray(decryptedAbstract);
					clearArray(decryptedKeywords);
					clearArray(decryptedBody);
					clearArray(decryptedReferences);
				} else {
					System.out.println("Article not found with ID: " + articleId);
				}
			}
		}
	}

	/**
	 * Helper method to clear the data
	 * 
	 * @param array The array which holds decrypted data to be deleted.
	 */

	private void clearArray(char[] array) {
		Arrays.fill(array, ' '); // Overwrite char array with blanks
	}

	/**
	 * Helper method to close the connection. Ensures that, upon the program again,
	 * we do not encounter an error that the database is already in use.
	 * 
	 */
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
