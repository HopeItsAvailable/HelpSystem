package helpSystem;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Junit_Shubhdeep {

    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:~/helpSystemDB";
    private static final String USER = "sa";
    private static final String PASS = "";
    private static Connection connection;
    private static DatabaseHelperArticle databaseHelperArticle;

    @BeforeAll
    public static void connectToDatabase() throws Exception {
        System.out.println("Connecting to database...");
        Class.forName(JDBC_DRIVER);
        connection = DriverManager.getConnection(DB_URL, USER, PASS);
        databaseHelperArticle = new DatabaseHelperArticle();
        databaseHelperArticle.connectToDatabase();

        System.out.println("Creating tables for testing...");
        createTables();
        System.out.println("Database setup completed.");
    }

    private static void createTables() throws SQLException {
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

        String userTable = """
            CREATE TABLE IF NOT EXISTS cse360Users (
                id INT AUTO_INCREMENT PRIMARY KEY,
                username VARCHAR(255),
                password VARCHAR(255),
                user_role VARCHAR(255)
            );
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(articleTable);
            stmt.execute(userTable);
            System.out.println("Tables created successfully.");
        }
    }

    @BeforeEach
    public void setupArticlesAndUsers() throws SQLException {
        System.out.println("Inserting mock articles and users...");
        String insertArticle = """
            INSERT INTO cse360Articles (title, author, paper_abstract, keywords, body, references, level, articleGroup)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?);
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(insertArticle)) {
            pstmt.setString(1, "Test Article 1");
            pstmt.setString(2, "Author1");
            pstmt.setString(3, "Abstract 1");
            pstmt.setString(4, "Test, Java");
            pstmt.setString(5, "Body of article 1");
            pstmt.setString(6, "None");
            pstmt.setString(7, "Intermediate");
            pstmt.setString(8, "Group1");
            pstmt.executeUpdate();

            pstmt.setString(1, "Test Article 2");
            pstmt.setString(2, "Author2");
            pstmt.setString(3, "Abstract 2");
            pstmt.setString(4, "Test, Python");
            pstmt.setString(5, "Body of article 2");
            pstmt.setString(6, "None");
            pstmt.setString(7, "Advanced");
            pstmt.setString(8, "Group2");
            pstmt.executeUpdate();
        }

        String insertUser = """
            INSERT INTO cse360Users (username, password, user_role)
            VALUES (?, ?, ?);
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
            pstmt.setString(1, "studentUser");
            pstmt.setString(2, "password1");
            pstmt.setString(3, "Student");
            pstmt.executeUpdate();

            pstmt.setString(1, "instructorUser");
            pstmt.setString(2, "password2");
            pstmt.setString(3, "Instructor");
            pstmt.executeUpdate();

            pstmt.setString(1, "adminUser");
            pstmt.setString(2, "password3");
            pstmt.setString(3, "Admin");
            pstmt.executeUpdate();
        }

        System.out.println("Mock data inserted successfully.");
    }

    @AfterEach
    public void cleanupData() throws SQLException {
        System.out.println("Cleaning up data after test...");
        String deleteArticles = "DELETE FROM cse360Articles";
        String deleteUsers = "DELETE FROM cse360Users";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(deleteArticles);
            stmt.execute(deleteUsers);
        }
        System.out.println("Data cleanup completed.");
    }

    @AfterAll
    public static void tearDownDatabase() throws SQLException {
        System.out.println("Closing database connection...");
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        System.out.println("Database connection closed.");
    }

    @Test
    public void testSearchArticlesByTitle() throws SQLException {
        System.out.println("Testing search by title...");
        String searchQuery = "SELECT * FROM cse360Articles WHERE title LIKE ?";
        String searchTerm = "Test Article 1";

        try (PreparedStatement pstmt = connection.prepareStatement(searchQuery)) {
            pstmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = pstmt.executeQuery();

            assertTrue(rs.next(), "Article should be found by title");
            System.out.println("Article found: " + rs.getString("title"));
            assertEquals("Test Article 1", rs.getString("title"), "Title should match search term");
        }
    }

    @Test
    public void testSearchArticlesByAuthor() throws SQLException {
        System.out.println("Testing search by author...");
        String searchQuery = "SELECT * FROM cse360Articles WHERE author LIKE ?";
        String searchTerm = "Author2";

        try (PreparedStatement pstmt = connection.prepareStatement(searchQuery)) {
            pstmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = pstmt.executeQuery();

            assertTrue(rs.next(), "Article should be found by author");
            System.out.println("Article author found: " + rs.getString("author"));
            assertEquals("Author2", rs.getString("author"), "Author should match search term");
        }
    }

    @Test
    public void testViewArticleDetailsByInstructor() throws SQLException {
        System.out.println("Testing view article details by instructor...");
        String userRole = getUserRole("instructorUser");
        System.out.println("Role of instructorUser: " + userRole);
        if ("Instructor".equals(userRole)) {
            String searchQuery = "SELECT * FROM cse360Articles WHERE title LIKE ?";
            String searchTerm = "Test Article 1";

            try (PreparedStatement pstmt = connection.prepareStatement(searchQuery)) {
                pstmt.setString(1, "%" + searchTerm + "%");
                ResultSet rs = pstmt.executeQuery();

                assertTrue(rs.next(), "Article should be found by title");
                System.out.println("Instructor viewing article: " + rs.getString("title"));
                assertEquals("Test Article 1", rs.getString("title"), "Title should match search term");
                assertEquals("Body of article 1", rs.getString("body"), "Body should match expected");
            }
        } else {
            fail("User does not have Instructor role.");
        }
    }

    @Test
    public void testViewArticleDetailsByStudent() throws SQLException {
        System.out.println("Testing view article details by student...");
        String userRole = getUserRole("studentUser");
        System.out.println("Role of studentUser: " + userRole);
        if ("Student".equals(userRole)) {
            String searchQuery = "SELECT * FROM cse360Articles WHERE title LIKE ?";
            String searchTerm = "Test Article 1";

            try (PreparedStatement pstmt = connection.prepareStatement(searchQuery)) {
                pstmt.setString(1, "%" + searchTerm + "%");
                ResultSet rs = pstmt.executeQuery();

                assertTrue(rs.next(), "Article should be found by title");
                System.out.println("Student viewing article: " + rs.getString("title"));
                assertEquals("Test Article 1", rs.getString("title"), "Title should match search term");
                assertEquals("Body of article 1", rs.getString("body"), "Body should match expected");
            }
        } else {
            fail("User does not have Student role.");
        }
    }

    private String getUserRole(String username) throws SQLException {
        System.out.println("Getting role for user: " + username);
        String roleQuery = "SELECT user_role FROM cse360Users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(roleQuery)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("user_role");
            }
            return null;
        }
    }
}
