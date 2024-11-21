package helpSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Junit {

    private Article article;

    @BeforeEach
    public void setUp() {
        // Create a sample Article object before each test
        article = new Article(1, "Test Title", "Author Name", "Test Abstract", 
                              "Test Keywords", "Test Body", "Test References", "Intermediate");
    }

    @Test
    public void testGetId() {
        assertEquals(1, article.getId());
    }

    @Test
    public void testGetTitle() {
        assertEquals("Test Title", article.getTitle());
    }

    @Test
    public void testGetAuthor() {
        assertEquals("Author Name", article.getAuthor());
    }

    @Test
    public void testGetPaperAbstract() {
        assertEquals("Test Abstract", article.getPaperAbstract());
    }

    @Test
    public void testGetKeywords() {
        assertEquals("Test Keywords", article.getKeywords());
    }

    @Test
    public void testGetBody() {
        assertEquals("Test Body", article.getBody());
    }

    @Test
    public void testGetReferences() {
        assertEquals("Test References", article.getReferences());
    }

    @Test
    public void testGetLevel() {
        assertEquals("Intermediate", article.getLevel());
    }
}
