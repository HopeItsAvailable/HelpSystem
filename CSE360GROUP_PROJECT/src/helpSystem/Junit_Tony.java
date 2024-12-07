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

        // Create new article groups
        dbHelper.addArticleGroup("Tech Group");
        dbHelper.addArticleGroup("Science Group");
        dbHelper.addArticleGroup("Health Group");
        dbHelper.addArticleGroup("Education Group");
    }

    @AfterEach
    public void tearDown() throws Exception {
        // Clean up by deleting groups
        dbHelper.deleteArticleGroup("Tech Group");
        dbHelper.deleteArticleGroup("Science Group");
        dbHelper.deleteArticleGroup("Health Group");
        dbHelper.deleteArticleGroup("Education Group");

        dbHelper.closeConnection();
    }

    @Test
    public void testBackupAndRestoreGroup() throws SQLException {
        // Backup the "Education Group" before deletion
        dbHelper.backupArticleGroupsToFile("Education Group");

        // Delete "Education Group"
        dbHelper.deleteArticleGroup("Education Group");

        // Check that "Education Group" was deleted
        boolean doesGroupExistBeforeRestore = dbHelper.doesGroupExist("Education Group");
        assertFalse(doesGroupExistBeforeRestore);

        // Restore the "Education Group" from backup
        dbHelper.restoreArticleGroupsFromFile("Education Group");

        // Check if the "Education Group" is restored
        boolean doesGroupExistAfterRestore = dbHelper.doesGroupExist("Education Group");
        assertTrue(doesGroupExistAfterRestore);
    }