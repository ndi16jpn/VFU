package database;

import organisations.Place;
import organisations.Region;
import organisations.Unit;
import roles.Handledare;
import roles.Student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static database.DDL.*;

class DeleteDbContent implements DatabaseDeleter {

    private String dbUrl;
    private Properties sqLiteConfig;

    DeleteDbContent(String dbUrl, Properties sqLiteConfig) {
        this.dbUrl = dbUrl;
        this.sqLiteConfig = sqLiteConfig;
    }

    private void deleteDbContent(String tableName) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM " + tableName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void deleteAllPlaceContent() throws DatabaseException {
        deleteDbContent(PLACE_TABLE);
    }

    @Override
    public void deleteAllRegionContent() throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM " + REGION_TABLE + " WHERE name != 'no_region'");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void deleteAllMuniContent() throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM " + MUNI_TABLE + " WHERE name != no_muni and region != no_region");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void deleteAllUnitContent() throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM " + UNIT_TABLE + " WHERE municipality != no_muni and name != no_choice");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void deleteAllStudentContent() throws DatabaseException {
        deleteDbContent(STUDENT_TABLE);
    }
    @Override
    public void deleteAllStudentFromPlaceTable(List<Student> students) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE " + PLACE_TABLE +
                            " SET " + PLACE_COLUMN_STUDENT + " = null " +
                            " WHERE student = ?"
            );
            for(Student student : students){
                preparedStatement.setString(1, student.getEmail());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }
    @Override
    public void deleteAllHandledareFromPlaceTable(List<Handledare> handledares) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE " + PLACE_TABLE +
                            " SET " + PLACE_COLUMN_HANDLEDARE + " = null " +
                            " WHERE handledare = ?"
            );
            for(Handledare handledare : handledares){
                preparedStatement.setString(1, handledare.getEmail());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void deleteAllVFUSamContent() throws DatabaseException {
        deleteDbContent(VFU_SAM_TABLE);
    }

    @Override
    public void deleteAllStudentDataContent() throws DatabaseException {
        deleteDbContent(STUDENT_DATA_TABLE);
    }

    @Override
    public void deleteAllHandledareContent() throws DatabaseException {
        deleteDbContent(HANDLEDARE_TABLE);
    }

    @Override
    public void deletePlaceContent(Place place) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM " + PLACE_TABLE + " WHERE id = ?"
            );
            preparedStatement.setInt(1, place.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }
    @Override
    public void deleteAllPlacesForUnit(Unit unit) throws DatabaseException{
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM " + PLACE_TABLE + " WHERE unit = ?"
            );

            preparedStatement.setInt(1, unit.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }


    @Override
    public void deleteRegionAndMuniContent(Region region) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatementMunis = connection.prepareStatement(
                    "DELETE FROM " + MUNI_TABLE + " WHERE " + MUNI_COLUMN_REGION + "= ?"
            );
            preparedStatementMunis.setString(1, region.getName());
            preparedStatementMunis.executeUpdate();

            PreparedStatement preparedStatementRegion = connection.prepareStatement(
                    "DELETE FROM " + REGION_TABLE + " WHERE " + REGION_COLUMN_NAME + "= ?"
            );
            preparedStatementRegion.setString(1, region.getName());
            preparedStatementRegion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void removeMuniFromRegion(String municipality, String region) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement deleteFromRegionMunipreparedStatement = connection.prepareStatement(
                    "DELETE FROM " + REGION_MUNI_TABLE + " WHERE " + REGION_MUNI_COLUMN_MUNI + "= ?"
                            + " AND " + REGION_MUNI_COLUMN_REGION + " = ?"
            );
            deleteFromRegionMunipreparedStatement.setString(1, municipality);
            deleteFromRegionMunipreparedStatement.setString(2, region);
            deleteFromRegionMunipreparedStatement.executeUpdate();

            if(!DatabaseHandler.getDatabase().getSelector().municipalityHasRegions(municipality)) {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM " + MUNI_TABLE + " WHERE " + MUNI_COLUMN_NAME + "= ?"
                );
                preparedStatement.setString(1, municipality);
                preparedStatement.executeUpdate();
            }




        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void deleteMuniContent(String municipality) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement deleteFromRegionMunipreparedStatement = connection.prepareStatement(
                    "DELETE FROM " + REGION_MUNI_TABLE + " WHERE " + REGION_MUNI_COLUMN_MUNI + "= ?"
            );
            deleteFromRegionMunipreparedStatement.setString(1, municipality);

            deleteFromRegionMunipreparedStatement.executeUpdate();

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM " + MUNI_TABLE + " WHERE " + MUNI_COLUMN_NAME + "= ?"
            );
            preparedStatement.setString(1, municipality);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void deleteUnitContent(Unit unit) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM " + UNIT_TABLE + " WHERE id= ?"
            );
            preparedStatement.setInt(1, unit.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void deleteStudentContent(String studentEmail) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM " + STUDENT_TABLE + " WHERE email= ?"
            );
            preparedStatement.setString(1, studentEmail);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void deleteVFUSamContent(String email) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            String sql = "DELETE FROM " + VFU_SAM_TABLE
                    + " WHERE " + VFU_COLUMN_EMAIL + "=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void deleteStudentDataContent(String studentEmail) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM " + STUDENT_DATA_TABLE + " WHERE email= ?"
            );
            preparedStatement.setString(1, studentEmail);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void deleteHandledareContent(Handledare handledare) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatementReferenceTable = connection.prepareStatement(
                    "DELETE FROM " + PLACE_HANDLEDARE_TABLE + " WHERE "+ HANDLEDARE_REFERENCE_COLUMN +"= ?"
            );
            PreparedStatement preparedStatementHandledareTable = connection.prepareStatement(
                    "DELETE FROM " + HANDLEDARE_TABLE + " WHERE "+ HANDLEDARE_COLUMN_EMAIL +"= ?"
            );
            preparedStatementReferenceTable.setString(1, handledare.getEmail());
            preparedStatementHandledareTable.setString(1, handledare.getEmail());
            preparedStatementReferenceTable.executeUpdate();
            preparedStatementHandledareTable.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void deleteHandledareFromPlace(Handledare handledare) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE " + PLACE_TABLE +
                            " SET " + PLACE_COLUMN_HANDLEDARE + " = null " +
                            " WHERE handledare = ?"
            );
            preparedStatement.setString(1, handledare.getEmail());
            preparedStatement.executeUpdate();

            PreparedStatement removeHandledareStatement = connection.prepareStatement("DELETE FROM " + HANDLEDARE_TABLE + " where " + HANDLEDARE_COLUMN_EMAIL + " = ?");
            removeHandledareStatement.setString(1, handledare.getEmail());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void deleteStudentFromPlace(String studentEmail) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE " + PLACE_TABLE +
                            " SET " + PLACE_COLUMN_STUDENT + " = null " +
                            " WHERE student = ?"
            );
            preparedStatement.setString(1, studentEmail);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }
    @Override
    public void deleteSinglePlace(Place place) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM " + PLACE_TABLE + " WHERE unit = ? and id = ? and " + PLACE_COLUMN_STUDENT + " IS NULL "
            );
            preparedStatement.setInt(1, place.getUnit().getId());
            preparedStatement.setInt(2, place.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void deleteLinkMailVFUContent(String regLink) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            String sql = "DELETE FROM " + LINK_MAIL_VFU_TABLE
                    + " WHERE " + LINK_MAIL_HANDLEDARE_COLUMN_REG_LINK + "=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, regLink);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void deleteLinkMailHandledareContent(String regLink) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM " + LINK_MAIL_HANDLEDARE_TABLE + " WHERE reg_link= ?"
            );
            preparedStatement.setString(1, regLink);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public void deleteAllHandledareRegistrationEmails() throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM " + MAIL_HANDLEDARE_REGISTRATION_MAIL_TABLE
            );
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

}
