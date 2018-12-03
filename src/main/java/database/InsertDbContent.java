package database;

import data.LinkMailHandledare;
import data.LinkMailVFUSamordnare;
import data.StudentData;
import organisations.Municipality;
import organisations.Place;
import organisations.Region;
import organisations.Unit;
import roles.Handledare;
import roles.Student;
import roles.VFUSamordnare;

import java.sql.*;
import java.util.List;
import java.util.Properties;

import static database.DDL.*;

class InsertDbContent implements DatabaseInserter {

    private String dbUrl;
    private Properties sqLiteConfig;

    InsertDbContent(String dbUrl, Properties sqLiteConfig) {
        this.dbUrl = dbUrl;
        this.sqLiteConfig = sqLiteConfig;
    }

    @Override
    public void addStudentsViaCSV(List<StudentData> studentData) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " + STUDENT_DATA_TABLE
                            + "(" + STUDENT_DATA_COLUMN_NAME + ","
                            + STUDENT_DATA_COLUMN_EMAIL + ","
                            + STUDENT_DATA_COLUMN_DOB + ","
                            + STUDENT_DATA_COLUMN_PHONENUMBER +")"
                            + "VALUES(?, ?, ?,?)"
            );
            for(StudentData student : studentData) {
                preparedStatement.setString(1, student.getName());
                preparedStatement.setString(2, student.getEmail());
                preparedStatement.setString(3, student.getDob());
                preparedStatement.setString(4, student.getPhoneNumber());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void addSingleStudent(StudentData student) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " + STUDENT_DATA_TABLE
                            + "(" + STUDENT_DATA_COLUMN_NAME + ","
                            + STUDENT_DATA_COLUMN_EMAIL + ","
                            + STUDENT_DATA_COLUMN_DOB + ","
                            + STUDENT_DATA_COLUMN_PHONENUMBER +")"
                            + "VALUES(?, ?, ?,?)"
            );
            preparedStatement.setString(1, student.getName());
            preparedStatement.setString(2, student.getEmail());
            preparedStatement.setString(3, student.getDob());
            preparedStatement.setString(4, student.getPhoneNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void addStudentFirstPage(String email, Region region, String personalLetter) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " + STUDENT_TABLE + "("
                            + STUDENT_COLUMN_EMAIL + ","
                            + STUDENT_COLUMN_REGION + ","
                            + STUDENT_COLUMN_PERSONAL_LETTER + ")"
                            + "VALUES(?, ?, ?)"
            );
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, region.getName());
            preparedStatement.setString(3, personalLetter);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void setStudentChoices(String email, int[] choices) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE " + STUDENT_TABLE
                            + " SET "
                            + STUDENT_COLUMN_CHOICE_1 + "=?,"
                            + STUDENT_COLUMN_CHOICE_2 + "=?,"
                            + STUDENT_COLUMN_CHOICE_3 + "=?,"
                            + STUDENT_COLUMN_CHOICE_4 + "=?,"
                            + STUDENT_COLUMN_CHOICE_5 + "=?"
                            + " WHERE " + STUDENT_COLUMN_EMAIL + "=?"
            );
            for (int i = 0; i < choices.length; i++) {
                preparedStatement.setInt(i+1, choices[i]);
            }
            final int MAX_NUM_OF_CHOICES = 5;
            if (choices.length < MAX_NUM_OF_CHOICES) {
                for (int i = choices.length; i < MAX_NUM_OF_CHOICES; i++) {
                    preparedStatement.setInt(i+1, 0);
                }
            }
            preparedStatement.setString(6, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void addVFUSamordnare(VFUSamordnare samordnare) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " + VFU_SAM_TABLE
                            + "(" + VFU_COLUMN_NAME + ","
                            + VFU_COLUMN_EMAIL + ","
                            + VFU_COLUMN_HASHED_PASSWORD + ","
                            + VFU_COLUMN_REGION + ","
                            + VFU_COLUMN_PHONE_NUMBER +")"
                            + "VALUES(?, ?, ?,?,?)"
            );
            preparedStatement.setString(1, samordnare.getName());
            preparedStatement.setString(2, samordnare.getEmail());
            preparedStatement.setString(3, samordnare.getHashedPassword());
            preparedStatement.setString(4, samordnare.getRegion().getName());
            preparedStatement.setString(5, samordnare.getPhoneNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void addHandledare(Handledare handledare) throws DatabaseException {

        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " + HANDLEDARE_TABLE
                            + "(" + HANDLEDARE_COLUMN_NAME + ","
                            + HANDLEDARE_COLUMN_EMAIL + ","
                            + HANDLEDARE_COLUMN_HASHED_PASSWORD + ","
                            + HANDLEDARE_COLUMN_PHONE_NUMBER +")"
                            + "VALUES(?, ?, ?,?)"
            );
            preparedStatement.setString(1, handledare.getName());
            preparedStatement.setString(2, handledare.getEmail());
            preparedStatement.setString(3, handledare.getHashedPassword());
            preparedStatement.setString(4, handledare.getPhoneNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void addLinkMailHandledare(LinkMailHandledare linkMailHandledare) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " + LINK_MAIL_HANDLEDARE_TABLE
                            + "(" + LINK_MAIL_HANDLEDARE_COLUMN_REG_LINK + ","
                            + LINK_MAIL_HANDLEDARE_COLUMN_EMAIL + ")"

                            + "VALUES(?, ?)"
            );

            preparedStatement.setString(1,linkMailHandledare.getRegLink());
            preparedStatement.setString(2,linkMailHandledare.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void addLinkMailVFU(LinkMailVFUSamordnare linkMailVFUSamordnare) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " + LINK_MAIL_VFU_TABLE
                            + "(" + LINK_MAIL_VFU_COLUMN_REG_LINK + ","
                            + LINK_MAIL_VFU_COLUMN_EMAIL + ")"
                            + "VALUES(?, ?)"
            );
            preparedStatement.setString(1,linkMailVFUSamordnare.getRegLink());
            preparedStatement.setString(2,linkMailVFUSamordnare.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void addRegion(Region region) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " + REGION_TABLE
                            + "(" + REGION_COLUMN_NAME + ")"
                            + "VALUES(?)"
            );
            preparedStatement.setString(1,region.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void addMunicipality(Municipality municipality) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " + MUNI_TABLE
                            + "(" + MUNI_COLUMN_NAME + ","
                            + MUNI_COLUMN_REGION + ")"
                            + "VALUES(?, ?)"
            );
            preparedStatement.setString(1,municipality.getName());
            preparedStatement.setString(2,municipality.getRegion().getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public long addUnit(Unit unit) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " + UNIT_TABLE
                            + "(" + UNIT_COLUMN_NAME + ","
                            + UNIT_COLUMN_MUNICIPALITY + ","
                            + UNIT_COLUMN_INFO + ","
                            + UNIT_COLUMN_NUM_OF_SLOTS + ")"
                            + "VALUES(?, ?, ?, ?)"
            );
            preparedStatement.setString(1,unit.getName());
            preparedStatement.setString(2,unit.getMunicipality().getName());
            preparedStatement.setString(3,unit.getInfo());
            preparedStatement.setString(4,String.valueOf(unit.getNumOfSlots()));
            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            rs.next();
            return rs.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void addPlace(Place place) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " + PLACE_TABLE +
                            "("+ PLACE_COLUMN_UNIT + ")"
                            + "VALUES(?)"
            );
            for(int i = 0; i<place.getUnit().getNumOfSlots(); i++) {
                preparedStatement.setInt(1, place.getUnit().getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }
    @Override
    public void addSinglePlace(Place place) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " + PLACE_TABLE +
                            "("+ PLACE_COLUMN_UNIT + ")"
                            + "VALUES(?)"
            );

                preparedStatement.setInt(1, place.getUnit().getId());
                preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void updatePhonenumberHandledare(String phoneNumber ,String handledareEmail) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE " + HANDLEDARE_TABLE
                            + " SET " + HANDLEDARE_COLUMN_PHONE_NUMBER + " = ?"
                            + " WHERE " + HANDLEDARE_COLUMN_EMAIL + " = ?"
            );
            preparedStatement.setString(1, phoneNumber);
            preparedStatement.setString(2, handledareEmail);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }
    @Override
    public void addHandledareToPlace(Place place, Handledare handledare) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " + PLACE_HANDLEDARE_TABLE +
                            " VALUES ( ?, ?)"

            );
            preparedStatement.setInt(1,place.getId());
            preparedStatement.setString(2,handledare.getEmail());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void addStudentToPlace(Place place, Student student) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE " + PLACE_TABLE +
                            " SET " + PLACE_COLUMN_STUDENT + " = ? " +
                            "WHERE id = ?"

            );
            preparedStatement.setString(1,student.getEmail());
            preparedStatement.setInt(2,place.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void addStudentToPlace(int placeId, String studentEmail) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE " + PLACE_TABLE +
                            " SET " + PLACE_COLUMN_STUDENT + " = ?" +
                            "WHERE id = ?"
            );
            preparedStatement.setString(1, studentEmail);
            preparedStatement.setInt(2, placeId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void updatePhoneNumberStudent(String phoneNumber, String email) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE " + STUDENT_DATA_TABLE
                            + " SET " + STUDENT_DATA_COLUMN_PHONENUMBER + " = ?"
                            + " WHERE " + STUDENT_DATA_COLUMN_EMAIL + " = ?"
            );
            preparedStatement.setString(1, phoneNumber);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }
    @Override
    public void addOneNumberOfSlotsUnit(Unit unit) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE " + UNIT_TABLE
                            + " SET " + UNIT_COLUMN_NUM_OF_SLOTS + " = ?"
                            + " WHERE " + UNIT_COLUMN_ID + " = ?"
            );
            int newNumberOfSlots = unit.getNumOfSlots() + 1;
            preparedStatement.setInt(1, newNumberOfSlots);
            preparedStatement.setInt(2, unit.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }
    @Override
    public void deleteOneNumberOfSlotsUnit(Unit unit) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE " + UNIT_TABLE
                            + " SET " + UNIT_COLUMN_NUM_OF_SLOTS + " = ?"
                            + " WHERE " + UNIT_COLUMN_ID + " = ?"
            );
            int newNumberOfSlots = unit.getNumOfSlots() - 1;
            if(newNumberOfSlots < 0){
                 newNumberOfSlots = 0;
            }

            preparedStatement.setInt(1, newNumberOfSlots);
            preparedStatement.setInt(2, unit.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void setVfuSamordnarePassword(String email, String hashedPassword) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE " + VFU_SAM_TABLE
                            + " SET " + VFU_COLUMN_HASHED_PASSWORD + "=?"
                            + " WHERE " + VFU_COLUMN_EMAIL + "=?"
            );
            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

}
