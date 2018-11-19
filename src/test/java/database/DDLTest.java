package database;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sqlite.SQLiteConfig;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import static database.DDL.*;
import static org.junit.Assert.*;

public class DDLTest {

    private Connection connection;

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        String testDbPath = Paths.get(tmpFolder.getRoot().getAbsolutePath(), "testdb.sqlite").toString();
        String dbUrl = "jdbc:sqlite:" + testDbPath;
        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);
        DDL ddl = new DDL(dbUrl, config.toProperties());
        ddl.createTablesIfNotExists();
        connection = DriverManager.getConnection(dbUrl);
    }

    @Test
    public void checkTablesExists() throws Exception {
        Set<String> expectedTables = new HashSet<>();
        expectedTables.add(ADMIN_TABLE);
        expectedTables.add(STUDENT_DATA_TABLE);
        expectedTables.add(UNIT_TABLE);
        expectedTables.add(VFU_SAM_TABLE);
        expectedTables.add(HANDLEDARE_TABLE);
        expectedTables.add(MUNI_TABLE);
        expectedTables.add(REGION_TABLE);
        expectedTables.add(LINK_MAIL_VFU_TABLE);
        expectedTables.add(LINK_MAIL_HANDLEDARE_TABLE);
        expectedTables.add(STUDENT_TABLE);
        expectedTables.add(PLACE_TABLE);

        ResultSet rs = connection.createStatement().executeQuery("SELECT name FROM sqlite_master WHERE type='table'");
        Set<String> actualTables = new HashSet<>();
        while (rs.next()) {
            actualTables.add(rs.getString("name"));
        }

        assertEquals(expectedTables, actualTables);
    }

    @Test
    public void onlyOneAdmin() throws Exception {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM " + ADMIN_TABLE);
        rs.next();
        assertEquals(1, rs.getInt(1));
    }

    @Test
    public void defaultAdminCorrect() throws Exception {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT "
                + ADMIN_COLUMN_EMAIL + ","
                + ADMIN_COLUMN_NAME + ","
                + ADMIN_COLUMN_HASHED_PASSWORD
                + " FROM " + ADMIN_TABLE);
        rs.next();
        String email = rs.getString(ADMIN_COLUMN_EMAIL);
        String name = rs.getString(ADMIN_COLUMN_NAME);
        String hashedPassword = rs.getString(ADMIN_COLUMN_HASHED_PASSWORD);

        assertEquals("Vfuadmin", email);
        assertEquals("Vfuadmin", name);
        assertEquals("1000:868bb0bec884e0ba69d16fbb4a64b95788ad919cada7d60b:9d6378fca5fb77b6a281a584052ed43a19710076cec3e911", hashedPassword);
    }

    @Test
    public void defaultChoiceCorrect() throws Exception {
        final int DEFAULT_CHOICE_ID = 0;
        final String DEFAULT_CHOICE_MUNI = "no_muni";
        final String DEFAULT_CHOICE_NAME = "no_choice";
        final int DEFAULT_CHOICE_NUMOFSLOTS = 0;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT "
                + UNIT_COLUMN_ID + ","
                + UNIT_COLUMN_MUNICIPALITY + ","
                + UNIT_COLUMN_NUM_OF_SLOTS + ","
                + UNIT_COLUMN_NAME
                + " FROM " + UNIT_TABLE);
        rs.next();
        int choiceId = rs.getInt(UNIT_COLUMN_ID);
        String choiceMuni = rs.getString(UNIT_COLUMN_MUNICIPALITY);
        String choiceName = rs.getString(UNIT_COLUMN_NAME);
        int choiceNumOfSlots = rs.getInt(UNIT_COLUMN_NUM_OF_SLOTS);

        assertEquals(DEFAULT_CHOICE_ID, choiceId);
        assertEquals(DEFAULT_CHOICE_MUNI, choiceMuni);
        assertEquals(DEFAULT_CHOICE_NAME, choiceName);
        assertEquals(DEFAULT_CHOICE_NUMOFSLOTS, choiceNumOfSlots);
    }

}