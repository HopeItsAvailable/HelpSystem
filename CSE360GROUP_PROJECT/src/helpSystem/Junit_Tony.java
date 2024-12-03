package helpSystem;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;



public class Junit_Tony {
	
	private DatabaseHelperArticleGroups dbHelper;

    @BeforeEach
    public void setUp() throws Exception {
    	
        dbHelper = new DatabaseHelperArticleGroups();
        dbHelper.connectToDatabase();

        dbHelper.addArticleGroup("Tech Group");
        dbHelper.addArticleGroup("Science Group");
        dbHelper.addArticleGroup("Health Group");
    }

    @AfterEach
    public void tearDown() throws Exception {
        dbHelper.deleteArticleGroup("Tech Group");
        dbHelper.deleteArticleGroup("Science Group");
        dbHelper.deleteArticleGroup("Health Group");

        dbHelper.closeConnection();
    }

    @Test
    public void testGetNumberOfGroups() throws SQLException {
        int numberOfGroups = dbHelper.getNumberOfGroups();

        assertEquals(3, numberOfGroups);
    }
    
    @Test
    public void testDeleteGroupAndCheckCount() throws Exception {
        dbHelper.deleteArticleGroup("Tech Group");
        dbHelper.deleteArticleGroup("Science Group");
        dbHelper.deleteArticleGroup("Health Group");

        int numberOfGroups = dbHelper.getNumberOfGroups();
        
        assertEquals(0, numberOfGroups);
    }
    
    @Test
    public void testDoesGroupExist() throws SQLException {
    	
        boolean doesTechGroupExist = dbHelper.doesGroupExist("Tech Group");
                assertTrue(doesTechGroupExist);

        boolean doesNonExistentGroupExist = dbHelper.doesGroupExist("Gamer Group");
        assertFalse(doesNonExistentGroupExist);
    }
    
}
