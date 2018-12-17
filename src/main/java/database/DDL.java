package database;

import organisations.Municipality;
import organisations.Region;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Skapar en default databas
 */
class DDL {


    static final String MUNI_TABLE = "municipality";
    static final String MUNI_COLUMN_REGION = "region";
    static final String MUNI_COLUMN_NAME = "name";


    static final String ADMIN_TABLE = "admin";
    static final String ADMIN_COLUMN_ADMIN_ID = "admin_id";
    static final String ADMIN_COLUMN_EMAIL = "email";
    static final String ADMIN_COLUMN_NAME = "name";
    static final String ADMIN_COLUMN_HASHED_PASSWORD = "hashed_password";
    static final String CREATE_TABLE_ADMIN = "CREATE TABLE IF NOT EXISTS " + ADMIN_TABLE + "("
            + ADMIN_COLUMN_ADMIN_ID + " INTEGER PRIMARY KEY,"
            + ADMIN_COLUMN_EMAIL + " TEXT NOT NULL,"
            + ADMIN_COLUMN_NAME + " TEXT NOT NULL,"
            + ADMIN_COLUMN_HASHED_PASSWORD + " TEXT NOT NULL);";

    static final String STUDENT_DATA_TABLE = "student_data";
    static final String STUDENT_DATA_COLUMN_EMAIL = "email";
    static final String STUDENT_DATA_COLUMN_NAME = "name";
    static final String STUDENT_DATA_COLUMN_DOB = "dob";
    static final String STUDENT_DATA_COLUMN_PHONENUMBER = "phone_number";
    static final String CREATE_TABLE_STUDENT_DATA = "CREATE TABLE IF NOT EXISTS " + STUDENT_DATA_TABLE + "("
            + STUDENT_DATA_COLUMN_EMAIL + " TEXT PRIMARY KEY NOT NULL,"
            + STUDENT_DATA_COLUMN_NAME + " TEXT NOT NULL,"
            + STUDENT_DATA_COLUMN_DOB + " TEXT NOT NULL,"
            + STUDENT_DATA_COLUMN_PHONENUMBER + " TEXT NOT NULL);";

    static final String UNIT_TABLE = "unit";
    static final String UNIT_COLUMN_MUNICIPALITY = "municipality";
    static final String UNIT_COLUMN_NAME = "name";
    static final String UNIT_COLUMN_ID = "id";
    static final String UNIT_COLUMN_INFO = "info";
    static final String UNIT_COLUMN_NUM_OF_SLOTS = "num_of_slots";
    static final String UNIT_COLUMN_REGION = "region";
    static final String CREATE_TABLE_UNIT = "CREATE TABLE IF NOT EXISTS " + UNIT_TABLE + "("
            + UNIT_COLUMN_MUNICIPALITY + " TEXT NOT NULL, "
            + UNIT_COLUMN_REGION + " TEXT NOT NULL, "
            + UNIT_COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY,"
            + UNIT_COLUMN_NAME + " TEXT NOT NULL,"
            + UNIT_COLUMN_INFO + " TEXT,"
            + UNIT_COLUMN_NUM_OF_SLOTS + " INTEGER NOT NULL, "
            + "FOREIGN KEY(" + UNIT_COLUMN_MUNICIPALITY + ", "
            + UNIT_COLUMN_REGION + ")"
            + " REFERENCES " + MUNI_TABLE + "(" + MUNI_COLUMN_NAME + ", "
            + MUNI_COLUMN_REGION + ")"
            + ");";

    static final String VFU_SAM_TABLE = "vfu_sam";
    static final String VFU_COLUMN_EMAIL = "email";
    static final String VFU_COLUMN_NAME = "name";
    static final String VFU_COLUMN_REGION = "region";
    static final String VFU_COLUMN_PHONE_NUMBER = "phone_number";
    static final String VFU_COLUMN_HASHED_PASSWORD = "hashed_password";
    static final String CREATE_TABLE_VFU = "CREATE TABLE IF NOT EXISTS " + VFU_SAM_TABLE + "("
            + VFU_COLUMN_EMAIL + " TEXT PRIMARY KEY NOT NULL,"
            + VFU_COLUMN_PHONE_NUMBER + " TEXT, "
            + VFU_COLUMN_HASHED_PASSWORD + " TEXT NOT NULL,"
            + VFU_COLUMN_REGION + " TEXT NOT NULL REFERENCES region,"
            + VFU_COLUMN_NAME + " TEXT NOT NULL);";

    static final String HANDLEDARE_TABLE = "handledare";
    static final String HANDLEDARE_COLUMN_EMAIL = "email";
    static final String HANDLEDARE_COLUMN_NAME = "name";
    static final String HANDLEDARE_COLUMN_PHONE_NUMBER = "phone_number";
    static final String HANDLEDARE_COLUMN_HASHED_PASSWORD = "hashed_password";
    static final String CREATE_TABLE_HANDLEDARE = "CREATE TABLE IF NOT EXISTS " + HANDLEDARE_TABLE + "("
            + HANDLEDARE_COLUMN_EMAIL + " TEXT PRIMARY KEY NOT NULL,"
            + HANDLEDARE_COLUMN_PHONE_NUMBER + " TEXT, "
            + HANDLEDARE_COLUMN_HASHED_PASSWORD + " TEXT NOT NULL,"
            + HANDLEDARE_COLUMN_NAME + " TEXT);";


    static final String CREATE_TABLE_MUNI = "CREATE TABLE IF NOT EXISTS " + MUNI_TABLE + "("
            + MUNI_COLUMN_REGION + " TEXT NOT NULL REFERENCES region,"
            + MUNI_COLUMN_NAME + " TEXT NOT NULL, "
            + "PRIMARY KEY(" + MUNI_COLUMN_REGION + ", "
            + MUNI_COLUMN_NAME
            +"));";

    static final String REGION_TABLE = "region";
    static final String REGION_COLUMN_NAME = "name";
    static final String CREATE_TABLE_REGION = "CREATE TABLE IF NOT EXISTS " + REGION_TABLE + "("
            + REGION_COLUMN_NAME + " TEXT PRIMARY KEY NOT NULL);";

    static final String LINK_MAIL_VFU_TABLE = "link_mail_vfu";
    static final String LINK_MAIL_VFU_COLUMN_EMAIL = "email";
    static final String LINK_MAIL_VFU_COLUMN_REG_LINK = "reg_link";
    static final String CREATE_TABLE_LINK_MAIL_VFU = "CREATE TABLE IF NOT EXISTS " + LINK_MAIL_VFU_TABLE + "("
            + LINK_MAIL_VFU_COLUMN_REG_LINK + " TEXT PRIMARY KEY NOT NULL,"
            + LINK_MAIL_VFU_COLUMN_EMAIL + " TEXT NOT NULL);";

    static final String LINK_MAIL_HANDLEDARE_TABLE = "link_mail_handledare";
    static final String LINK_MAIL_HANDLEDARE_COLUMN_EMAIL = "email";
    static final String LINK_MAIL_HANDLEDARE_COLUMN_REG_LINK = "reg_link";
    static final String CREATE_TABLE_LINK_MAIL_HANDLEDARE = "CREATE TABLE IF NOT EXISTS " + LINK_MAIL_HANDLEDARE_TABLE + "("
            + LINK_MAIL_HANDLEDARE_COLUMN_REG_LINK + " TEXT PRIMARY KEY NOT NULL,"
            + LINK_MAIL_HANDLEDARE_COLUMN_EMAIL + " TEXT NOT NULL);";

    static final String STUDENT_TABLE = "student";
    static final String STUDENT_COLUMN_EMAIL = "email";
    static final String STUDENT_COLUMN_REGION = "region";
    static final String STUDENT_COLUMN_PERSONAL_LETTER = "personal_letter";
    static final String STUDENT_COLUMN_CHOICE_1 = "choice_1";
    static final String STUDENT_COLUMN_CHOICE_2 = "choice_2";
    static final String STUDENT_COLUMN_CHOICE_3 = "choice_3";
    static final String STUDENT_COLUMN_CHOICE_4 = "choice_4";
    static final String STUDENT_COLUMN_CHOICE_5 = "choice_5";
    static final String CREATE_TABLE_STUDENT = "CREATE TABLE IF NOT EXISTS " + STUDENT_TABLE + "("
            + STUDENT_COLUMN_EMAIL + " TEXT PRIMARY KEY NOT NULL REFERENCES student_data,"
            + STUDENT_COLUMN_REGION + " TEXT NOT NULL REFERENCES region,"
            + STUDENT_COLUMN_CHOICE_1 + " INTEGER DEFAULT 0 NOT NULL REFERENCES unit,"
            + STUDENT_COLUMN_CHOICE_2 + " INTEGER DEFAULT 0 REFERENCES unit,"
            + STUDENT_COLUMN_CHOICE_3 + " INTEGER DEFAULT 0 REFERENCES unit,"
            + STUDENT_COLUMN_CHOICE_4 + " INTEGER DEFAULT 0 REFERENCES unit,"
            + STUDENT_COLUMN_CHOICE_5 + " INTEGER DEFAULT 0 REFERENCES unit,"
            + STUDENT_COLUMN_PERSONAL_LETTER + " TEXT NOT NULL);";

    static final String PLACE_TABLE = "place";
    static final String PLACE_COLUMN_ID = "id";
    static final String PLACE_COLUMN_STUDENT = "student";
    static final String PLACE_COLUMN_HANDLEDARE = "handledare";
    static final String PLACE_COLUMN_UNIT = "unit";
    static final String CREATE_TABLE_PLACE = "CREATE TABLE IF NOT EXISTS " + PLACE_TABLE + "("
            + PLACE_COLUMN_ID + " INTEGER PRIMARY KEY NOT NULL,"
            + PLACE_COLUMN_STUDENT + " TEXT REFERENCES student, "
            + PLACE_COLUMN_HANDLEDARE + " TEXT REFERENCES handledare,"
            + PLACE_COLUMN_UNIT + " TEXT NOT NULL REFERENCES unit);";

    static final String PLACE_HANDLEDARE_TABLE = "place_handledare";
    static final String PLACE_REFERENCE_COLUMN = "place_id";
    static final String HANDLEDARE_REFERENCE_COLUMN = "handledare_email";
    static final String CREATE_TABLE_PLACE_HANDLEDARE = "CREATE TABLE IF NOT EXISTS " + PLACE_HANDLEDARE_TABLE + "("
            + PLACE_REFERENCE_COLUMN + " INTEGER NOT NULL REFERENCES " + PLACE_TABLE + ", "
            + HANDLEDARE_REFERENCE_COLUMN + " TEXT NOT NULL REFERENCES " + HANDLEDARE_TABLE + ", "
            + "PRIMARY KEY ("+ PLACE_REFERENCE_COLUMN + "," + HANDLEDARE_REFERENCE_COLUMN + "));";

    static final String TEXT_CONTENT_STUDENT_FIRST_TABLE = "text_content_student_first";
    static final String TEXT_CONTENT_STUDENT_FIRST_PARAGRAPH_COLUMN = "paragraph";
    static final String TEXT_CONTENT_STUDENT_FIRST_CONTENT_COLUMN = "content";
    static final String TEXT_CONTENT_STUDENT_FIRST_CONTENT_P1_DEFAULT = "\"När du fyllt i dina uppgifter och skrivit ett personligt brev, kommer du att kunna göra 5 val till VFU-platser du önskar göra din VFU på. Du börjar med ditt förstahandsval, sedan ditt andrahandsval osv, alltså i den ordning du önskar platserna. Du gör dina val på den studieort du tillhör.\"";
    static final String TEXT_CONTENT_STUDENT_FIRST_CONTENT_P2_DEFAULT = "\"Observera! Läs noga igenom platsbeskrivningar, om det finns särskilda krav om t ex ålder.\n" +
            "                    Läs också noga igenom informationen på den här sidan innan du börjar.\"";
    static final String TEXT_CONTENT_STUDENT_FIRST_CONTENT_P3_DEFAULT = "\"Om flera studenter har gjort samma förstahandsval, fördelar systemet platserna slumpvis. Samma gäller med valen som följer efter.\"";
    static final String CREATE_TABLE_TEXT_CONTET_STUDENT_FIRST = "CREATE TABLE IF NOT EXISTS " +
            TEXT_CONTENT_STUDENT_FIRST_TABLE +
            "(" + TEXT_CONTENT_STUDENT_FIRST_PARAGRAPH_COLUMN + " INTEGER NOT NULL, " +
            TEXT_CONTENT_STUDENT_FIRST_CONTENT_COLUMN + " TEXT, " +
            "PRIMARY KEY (" + TEXT_CONTENT_STUDENT_FIRST_PARAGRAPH_COLUMN + "))";

    private String dbUrl;
    private Properties sqLiteConfig;

    DDL(String dbUrl, Properties sqLiteConfig) {
        this.dbUrl = dbUrl;
        this.sqLiteConfig = sqLiteConfig;
    }

    private  List<String> fillRegionList(){
        List<String> allRegions = new ArrayList<>();

        allRegions.add("Gävle");
        allRegions.add("Bollnäs");
        allRegions.add("Hudiksvall");
        allRegions.add("Södertälje");
        allRegions.add("Norrtälje");

        return allRegions;
    }
    private  List<Municipality> fillMuniList(){
        List<Municipality> allMunis = new ArrayList<>();

        allMunis.add(new Municipality("Gävle", new Region("Gävle")));
        allMunis.add(new Municipality("Hofors", new Region("Gävle")));
        allMunis.add(new Municipality("Sandviken", new Region("Gävle")));
        allMunis.add(new Municipality("Ockelbo", new Region("Gävle")));

        allMunis.add(new Municipality("Söderhamn", new Region("Bollnäs")));
        allMunis.add(new Municipality("Bollnäs", new Region("Bollnäs")));
        allMunis.add(new Municipality("Ovanåker", new Region("Bollnäs")));

        allMunis.add(new Municipality("Hudiksvall", new Region("Hudiksvall")));
        allMunis.add(new Municipality("Nordanstig", new Region("Hudiksvall")));
        allMunis.add(new Municipality("Ljusdal", new Region("Hudiksvall")));

        allMunis.add(new Municipality("Södertälje", new Region("Södertälje")));

        allMunis.add(new Municipality("Norrtälje", new Region("Norrtälje")));


        return allMunis;
    }

    void createTablesIfNotExists() {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(CREATE_TABLE_LINK_MAIL_HANDLEDARE);
            statement.executeUpdate(CREATE_TABLE_LINK_MAIL_VFU);
            statement.executeUpdate(CREATE_TABLE_HANDLEDARE);
            statement.executeUpdate(CREATE_TABLE_VFU);
            statement.executeUpdate(CREATE_TABLE_ADMIN);
            statement.executeUpdate(CREATE_TABLE_STUDENT);
            statement.executeUpdate(CREATE_TABLE_STUDENT_DATA);
            statement.executeUpdate(CREATE_TABLE_REGION);
            statement.executeUpdate(CREATE_TABLE_MUNI);
            statement.executeUpdate(CREATE_TABLE_UNIT);

            statement.executeUpdate(CREATE_TABLE_PLACE);

            statement.executeUpdate(CREATE_TABLE_PLACE_HANDLEDARE);
            statement.executeUpdate(CREATE_TABLE_TEXT_CONTET_STUDENT_FIRST);
            //tillfällig lösning för att illustrera

            if (!statement.executeQuery("SELECT * FROM " + TEXT_CONTENT_STUDENT_FIRST_TABLE
                    + " WHERE " + TEXT_CONTENT_STUDENT_FIRST_PARAGRAPH_COLUMN + " = 1").next()) {
               statement.executeUpdate("INSERT INTO " + TEXT_CONTENT_STUDENT_FIRST_TABLE + " VALUES(1, " +
                       TEXT_CONTENT_STUDENT_FIRST_CONTENT_P1_DEFAULT + ")");
            }
            if (!statement.executeQuery("SELECT * FROM " + TEXT_CONTENT_STUDENT_FIRST_TABLE
                    + " WHERE " + TEXT_CONTENT_STUDENT_FIRST_PARAGRAPH_COLUMN + " = 2").next()) {
                statement.executeUpdate("INSERT INTO " + TEXT_CONTENT_STUDENT_FIRST_TABLE + " VALUES(2, " +
                        TEXT_CONTENT_STUDENT_FIRST_CONTENT_P2_DEFAULT + ")");
            }
            if (!statement.executeQuery("SELECT * FROM " + TEXT_CONTENT_STUDENT_FIRST_TABLE
                    + " WHERE " + TEXT_CONTENT_STUDENT_FIRST_PARAGRAPH_COLUMN + " = 3").next()) {
                statement.executeUpdate("INSERT INTO " + TEXT_CONTENT_STUDENT_FIRST_TABLE + " VALUES(3, " +
                        TEXT_CONTENT_STUDENT_FIRST_CONTENT_P3_DEFAULT + ")");
            }


            if (!adminUserExists(connection)) {
                createDefaultAdmin(connection);
                createDefaultRegion(connection);
                createDefaultMuni(connection);
                createDefaultChoice(connection);
                fillRegionsTable(connection, fillRegionList());
                fillMuniTable(connection, fillMuniList());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("database error", e);
        }
    }

    private boolean adminUserExists(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM " + ADMIN_TABLE);
        boolean adminUserExists = rs.next();
        return adminUserExists;
    }

    private void createDefaultAdmin(Connection connection) throws SQLException {
        final String DEFAULT_ADMIN_EMAIL = "Vfuadmin";
        final String DEFAULT_ADMIN_NAME = "Vfuadmin";
        final String DEFAULT_ADMIN_PASSWORD =
                "1000:868bb0bec884e0ba69d16fbb4a64b95788ad919cada7d60b:9d6378fca5fb77b6a281a584052ed43a19710076cec3e911";
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO " + ADMIN_TABLE
                        + "(" + ADMIN_COLUMN_EMAIL + ","
                        + ADMIN_COLUMN_NAME + ","
                        + ADMIN_COLUMN_HASHED_PASSWORD + ")"
                        + "VALUES(?, ?, ?)"
        );
        preparedStatement.setString(1, DEFAULT_ADMIN_EMAIL);
        preparedStatement.setString(2, DEFAULT_ADMIN_NAME);
        preparedStatement.setString(3, DEFAULT_ADMIN_PASSWORD);
        preparedStatement.executeUpdate();
    }

    private void createDefaultChoice(Connection connection) throws SQLException {
        final int DEFAULT_CHOICE_ID = 0;
        final String DEFAULT_CHOICE_MUNI = "no_muni";
        final String DEFAULT_CHOICE_NAME = "no_choice";
        final int DEFAULT_CHOICE_NUMOFSLOTS = 0;

        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO " + UNIT_TABLE
                        + "(" + UNIT_COLUMN_ID + ","
                        + UNIT_COLUMN_MUNICIPALITY + ","
                        + UNIT_COLUMN_NUM_OF_SLOTS + ","
                        + UNIT_COLUMN_NAME + ")"
                        + "VALUES(?, ?, ?,?)"
        );
        preparedStatement.setInt(1, DEFAULT_CHOICE_ID);
        preparedStatement.setString(2, DEFAULT_CHOICE_MUNI);
        preparedStatement.setInt(3, DEFAULT_CHOICE_NUMOFSLOTS);
        preparedStatement.setString(4, DEFAULT_CHOICE_NAME);
        preparedStatement.executeUpdate();
    }

    private void createDefaultMuni(Connection connection) throws SQLException {
        final String DEFAULT_MUNI_REGION = "no_region";
        final String DEFAULT_MUNI_NAME = "no_muni";

        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO " + MUNI_TABLE
                        + "(" + MUNI_COLUMN_REGION + ","
                        + MUNI_COLUMN_NAME + ")"
                        + "VALUES(?, ?)"
        );
        preparedStatement.setString(1, DEFAULT_MUNI_REGION);
        preparedStatement.setString(2, DEFAULT_MUNI_NAME);
        preparedStatement.executeUpdate();
    }

    private void createDefaultRegion(Connection connection) throws SQLException {
        final String DEFAULT_REGION_NAME = "no_region";

        PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " + REGION_TABLE
                            + "("
                            + REGION_COLUMN_NAME + ")"
                            + "VALUES(?)"
        );
        preparedStatement.setString(1, DEFAULT_REGION_NAME);
        preparedStatement.executeUpdate();
    }
    private void fillRegionsTable(Connection connection, List<String> regions) throws SQLException {
        connection.setAutoCommit(false);
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO " + REGION_TABLE
                        + "("
                        + REGION_COLUMN_NAME + ")"
                        + "VALUES(?)"

        );
        for(String region : regions){
            preparedStatement.setString(1, region);
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        connection.commit();

    }

    private void fillMuniTable(Connection connection, List<Municipality> munis) throws SQLException {
        connection.setAutoCommit(false);
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO " + MUNI_TABLE
                        + "(" + MUNI_COLUMN_REGION + ","
                        + MUNI_COLUMN_NAME + ")"
                        + "VALUES(?, ?)"
        );
        for(Municipality muni : munis){
            preparedStatement.setString(1, muni.getRegion().getName());
            preparedStatement.setString(2, muni.getName());
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        connection.commit();

    }

}
