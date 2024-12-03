package helpSystem;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JUnitTests {

    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/userDatabase";

    static final String USER = "sa";
    static final String PASS = "";

    private Connection connection;
    private static DatabaseHelperUser databaseHelper;
    private static DatabaseHelperArticle databaseHelper1;

    @BeforeAll
    public void connectToDatabase() throws Exception {
        Class.forName(JDBC_DRIVER);
        connection = DriverManager.getConnection(DB_URL, USER, PASS);
        databaseHelper = new DatabaseHelperUser();
        databaseHelper.connectToDatabase();
        databaseHelper1 = new DatabaseHelperArticle();
        databaseHelper1.connectToDatabase();

        // Create tables
        createTables();
    }

    private void createTables() throws SQLException {
        String userTable = """
            CREATE TABLE IF NOT EXISTS cse360users (
                id INT AUTO_INCREMENT PRIMARY KEY,
                username VARCHAR(50) UNIQUE,
                password VARCHAR(255),
                email VARCHAR(255) UNIQUE,
                firstName VARCHAR(50),
                middleName VARCHAR(50),
                lastName VARCHAR(50),
                preferredFirstName VARCHAR(50),
                isAdmin BOOLEAN DEFAULT FALSE,
                isStudent BOOLEAN DEFAULT FALSE,
                isInstructor BOOLEAN DEFAULT FALSE,
                userGroups VARCHAR(255)
            );
        """;

        String articleTable = """
            CREATE TABLE IF NOT EXISTS cse360Articles (
                id INT AUTO_INCREMENT PRIMARY KEY,
                title VARCHAR(255),
                author VARCHAR(255),
                paper_abstract VARCHAR(255),
                keywords VARCHAR(255),
                body VARCHAR(255),
                references VARCHAR(255),
                level VARCHAR(255),
                articleGroup VARCHAR(255)
            );
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(userTable);
            stmt.execute(articleTable);
        }
    }

    @BeforeEach
    public void setupInstructor() throws SQLException {
        String addInstructor = """
            MERGE INTO cse360users (username, isInstructor)
            KEY(username)
            VALUES ('instructor123', true);
        """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(addInstructor);
        }
    }

    @AfterEach
    public void cleanupData() throws SQLException {
        String deleteArticles = "DELETE FROM cse360Articles";
        String deleteInstructor = "DELETE FROM cse360users WHERE username = 'instructor123'";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(deleteArticles);
            stmt.execute(deleteInstructor);
        }
    }

    @AfterAll
    public void tearDownDatabase() throws SQLException {
        connection.close();
    }

    @Test
    public void testCreateNewArticle() throws Exception {
        // Inputs
    	System.out.println("BEFORE ADDING ARTICLES");
    	System.out.println(printArticles());
    	
        String title = "New Article";
        String body = "This is a test article body";
        String groupName = "groupXYZ";
        String author = "instructor123";
        String keywords = "test, article";
        String references = "None";
        String level = "Beginner";

        // Add article
        String addArticle = """
            INSERT INTO cse360Articles (title, author, paper_abstract, keywords, body, references, level, articleGroup)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?);
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(addArticle)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, "This is an abstract");
            pstmt.setString(4, keywords);
            pstmt.setString(5, body);
            pstmt.setString(6, references);
            pstmt.setString(7, level);
            pstmt.setString(8, groupName);
            pstmt.executeUpdate();
        }

        // Verify article creation
        String queryArticle = """
            SELECT title, author, paper_abstract, keywords, body, references, level, articleGroup
            FROM cse360Articles
            WHERE title = ? AND articleGroup = ? AND author = ?;
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(queryArticle)) {
            pstmt.setString(1, title);
            pstmt.setString(2, groupName);
            pstmt.setString(3, author);
            ResultSet rs = pstmt.executeQuery();

            assertTrue(rs.next(), "Article should exist in the database");
            assertEquals(title, rs.getString("title"), "Title should match input");
            assertEquals(author, rs.getString("author"), "Author should match input");
            assertEquals("This is an abstract", rs.getString("paper_abstract"), "Abstract should match input");
            assertEquals(keywords, rs.getString("keywords"), "Keywords should match input");
            assertEquals(body, rs.getString("body"), "Body should match input");
            assertEquals(references, rs.getString("references"), "References should match input");
            assertEquals(level, rs.getString("level"), "Level should match input");
            assertEquals(groupName, rs.getString("articleGroup"), "Group should match input");
        }
        System.out.println("AFTER ADDING ARTICLES");
        System.out.println(printArticles());
    }
    public String printArticles() throws SQLException {
        // StringBuilder to collect the article data as a string
        StringBuilder articles = new StringBuilder();

        // SQL query to fetch all articles
        String query = "SELECT title, author, paper_abstract, keywords, body, references, level, articleGroup FROM cse360Articles";
        
        // Prepare and execute the query
        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            // Loop through the results and append each article's details to the StringBuilder
            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                String paperAbstract = rs.getString("paper_abstract");
                String keywords = rs.getString("keywords");
                String body = rs.getString("body");
                String references = rs.getString("references");
                String level = rs.getString("level");
                String articleGroup = rs.getString("articleGroup");

                // Format the article details
                articles.append("Title: ").append(title).append("\n")
                        .append("Author: ").append(author).append("\n")
                        .append("Abstract: ").append(paperAbstract).append("\n")
                        .append("Keywords: ").append(keywords).append("\n")
                        .append("Body: ").append(body).append("\n")
                        .append("References: ").append(references).append("\n")
                        .append("Level: ").append(level).append("\n")
                        .append("Group: ").append(articleGroup).append("\n")
                        .append("-----------------------------------------------------\n");
            }
        }

        // Return the collected article data as a string
        return articles.toString();
    }
    
    
    @BeforeEach
    public void insertTestUser() throws SQLException {
        String insertOrUpdateUser = """
            MERGE INTO cse360users (username, isStudent)
            KEY(username)
            VALUES ('user123', true);
        """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(insertOrUpdateUser);
        }

    }



    @AfterEach
    public void cleanUpTestUser() throws SQLException {
        String deleteUser = "DELETE FROM cse360users WHERE username = 'user123'";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(deleteUser);
        }
    }

   
    @Test
    public void testAddAdminRole() throws Exception {
        // Add Admin role
    	databaseHelper.addAdminRole("user123");

        // Verify role addition
        String query = "SELECT isAdmin FROM cse360users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, "user123");
            ResultSet rs = pstmt.executeQuery();

            assertTrue(rs.next(), "User should exist in the database");
            assertTrue(rs.getBoolean("isAdmin"), "User should now have Admin role");
        }
    }

    @Test
    public void testAddInstructorRole() throws Exception {
        // Add Instructor role
    	databaseHelper.addInstructorRole("user123");

        // Verify role addition
        String query = "SELECT isInstructor FROM cse360users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, "user123");
            ResultSet rs = pstmt.executeQuery();

            assertTrue(rs.next(), "User should exist in the database");
            assertTrue(rs.getBoolean("isInstructor"), "User should now have Instructor role");
        }
    }

    @Test
    public void testMultipleRoles() throws Exception {
        // Add roles
    	System.out.println("BEFORE ADDING ROLES");
    	databaseHelper.printAllUsers();
    	databaseHelper.addAdminRole("user123");
    	databaseHelper.addInstructorRole("user123");
    	System.out.println("AFTER ADDING ROLES");
    	databaseHelper.printAllUsers();

        // Verify multiple roles
        String query = "SELECT isStudent, isAdmin, isInstructor FROM cse360users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, "user123");
            ResultSet rs = pstmt.executeQuery();

            assertTrue(rs.next(), "User should exist in the database");
            assertTrue(rs.getBoolean("isStudent"), "User should have Student role");
            assertTrue(rs.getBoolean("isAdmin"), "User should have Admin role");
            assertTrue(rs.getBoolean("isInstructor"), "User should have Instructor role");
        }

    }
}
//test

    
    