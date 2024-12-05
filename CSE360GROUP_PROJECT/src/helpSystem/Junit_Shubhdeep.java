package helpSystem;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Junit_Shubhdeep {

    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/helpSystemDB";
    static final String USER = "sa";
    static final String PASS = "";

    private Connection connection;
    private static DatabaseHelperArticle databaseHelperArticle;

    @BeforeAll
    public void connectToDatabase() throws Exception {
        Class.forName(JDBC_DRIVER);
        connection = DriverManager.getConnection(DB_URL, USER, PASS);
        databaseHelperArticle = new DatabaseHelperArticle();
        databaseHelperArticle.connectToDatabase();

        // Create the necessary tables for testing
        createTables();
    }

    private void createTables() throws SQLException {
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
            stmt.execute(articleTable);
        }
    }

    @BeforeEach
    public void setupArticles() throws SQLException {
        // Insert mock articles for search testing
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
    }

    @AfterEach
    public void cleanupData() throws SQLException {
        // Clean up articles after each test
        String deleteArticles = "DELETE FROM cse360Articles";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(deleteArticles);
        }
    }

    @AfterAll
    public void tearDownDatabase() throws SQLException {
        connection.close();
    }

    @Test
    public void testSearchArticlesByTitle() throws SQLException {
        String searchQuery = "SELECT * FROM cse360Articles WHERE title LIKE ?";
        String searchTerm = "Test Article 1";

        try (PreparedStatement pstmt = connection.prepareStatement(searchQuery)) {
            pstmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = pstmt.executeQuery();

            assertTrue(rs.next(), "Article should be found by title");
            assertEquals("Test Article 1", rs.getString("title"), "Title should match search term");
        }
    }

    @Test
    public void testSearchArticlesByAuthor() throws SQLException {
        String searchQuery = "SELECT * FROM cse360Articles WHERE author LIKE ?";
        String searchTerm = "Author2";

        try (PreparedStatement pstmt = connection.prepareStatement(searchQuery)) {
            pstmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = pstmt.executeQuery();

            assertTrue(rs.next(), "Article should be found by author");
            assertEquals("Author2", rs.getString("author"), "Author should match search term");
        }
    }

    @Test
    public void testViewArticleDetails() throws SQLException {
        String searchQuery = "SELECT * FROM cse360Articles WHERE title LIKE ?";
        String searchTerm = "Test Article 1";

        try (PreparedStatement pstmt = connection.prepareStatement(searchQuery)) {
            pstmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = pstmt.executeQuery();

            assertTrue(rs.next(), "Article should be found by title");
            assertEquals("Test Article 1", rs.getString("title"), "Title should match search term");
            assertEquals("Body of article 1", rs.getString("body"), "Body should match expected");
        }
    }
}
