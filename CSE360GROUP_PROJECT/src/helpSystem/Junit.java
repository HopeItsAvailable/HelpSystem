package helpSystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Junit{

    private Article article;

    @BeforeEach
    void setUp() {
        // Set up the test article instance before each test
        article = new Article(
                1,
                "The Future of AI in Healthcare",
                "Jane Doe, John Smith",
                "This paper explores the potential of AI in healthcare and its applications.",
                "AI, Healthcare, Future, Technology",
                "The body of the article would contain detailed content about the future of AI in healthcare.",
                "Doe, J. (2023). The Future of AI. Journal of AI Research.",
                "Advanced"
        );
    }

    @Test
    void testGetId() {
        // Test that the getId method returns the correct value
        assertEquals(1, article.getId());
    }

    @Test
    void testGetTitle() {
        // Test that the getTitle method returns the correct value
        assertEquals("The Future of AI in Healthcare", article.getTitle());
    }

    @Test
    void testGetAuthor() {
        // Test that the getAuthor method returns the correct value
        assertEquals("Jane Doe, John Smith", article.getAuthor());
    }

    @Test
    void testGetPaperAbstract() {
        // Test that the getPaperAbstract method returns the correct value
        assertEquals("This paper explores the potential of AI in healthcare and its applications.", article.getPaperAbstract());
    }

    @Test
    void testGetKeywords() {
        // Test that the getKeywords method returns the correct value
        assertEquals("AI, Healthcare, Future, Technology", article.getKeywords());
    }

    @Test
    void testGetBody() {
        // Test that the getBody method returns the correct value
        assertEquals("The body of the article would contain detailed content about the future of AI in healthcare.", article.getBody());
    }

    @Test
    void testGetReferences() {
        // Test that the getReferences method returns the correct value
        assertEquals("Doe, J. (2023). The Future of AI. Journal of AI Research.", article.getReferences());
    }

    @Test
    void testGetLevel() {
        // Test that the getLevel method returns the correct value
        assertEquals("Advanced", article.getLevel());
    }
}
