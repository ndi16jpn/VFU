package database;

import data.StudentData;
import organisations.Municipality;
import organisations.Place;
import organisations.Region;
import organisations.Unit;
import roles.Admin;
import roles.Handledare;
import roles.Student;
import roles.VFUSamordnare;

import java.sql.*;
import java.util.*;

import static database.DDL.*;

class SelectDbContent implements DatabaseSelector {

    private String dbUrl;
    private Properties sqLiteConfig;

    SelectDbContent(String dbUrl, Properties sqLiteConfig) {
        this.dbUrl = dbUrl;
        this.sqLiteConfig = sqLiteConfig;
    }

    @Override
    public Admin getAdmin() throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM " + ADMIN_TABLE);
            rs.next();
            String email = rs.getString(ADMIN_COLUMN_EMAIL);
            String name = rs.getString(ADMIN_COLUMN_NAME);
            String hashedPassword = rs.getString(ADMIN_COLUMN_HASHED_PASSWORD);
            return new Admin(email, name, hashedPassword);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }
    @Override
    public List<Place> getAllPlacesWithoutStudent() throws DatabaseException{
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            List<Place> places = new ArrayList<>();
            Statement statement = connection.createStatement();
            String sql = "SELECT * "
                    + " FROM " + PLACE_TABLE + " WHERE student is null";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                if(rs.getString(PLACE_COLUMN_HANDLEDARE) == null){
                    places.add(new Place( rs.getInt(PLACE_COLUMN_ID), getUnit(rs.getInt(PLACE_COLUMN_UNIT))));
                }else{
                    places.add(new Place( rs.getInt(PLACE_COLUMN_ID),getHandledare(rs.getString(PLACE_COLUMN_HANDLEDARE)), getUnit(rs.getInt(PLACE_COLUMN_UNIT))));
                }
            }
            return places;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }
    @Override
    public List<Place> getAllPlacesWithStudent() throws DatabaseException{
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            List<Place> places = new ArrayList<>();
            Statement statement = connection.createStatement();
            String sql = "SELECT * "
                    + " FROM " + PLACE_TABLE + " WHERE student is not null";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                if(rs.getString(PLACE_COLUMN_HANDLEDARE) == null){
                    places.add(new Place( rs.getInt(PLACE_COLUMN_ID),getStudent(rs.getString(PLACE_COLUMN_STUDENT)), getUnit(rs.getInt(PLACE_COLUMN_UNIT))));
                }else{
                    places.add(new Place( rs.getInt(PLACE_COLUMN_ID),getUnit(rs.getInt(PLACE_COLUMN_UNIT)),getStudent(rs.getString(PLACE_COLUMN_STUDENT)),getHandledare(rs.getString(PLACE_COLUMN_HANDLEDARE))));
                }
            }
            return places;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public  List<Handledare> getAllHandledare()throws DatabaseException{
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            List<Handledare> handledare = new ArrayList<>();
            Statement statement = connection.createStatement();
            String sql = "SELECT * "
                    + " FROM " + HANDLEDARE_TABLE;
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                handledare.add(new Handledare(
                        rs.getString(HANDLEDARE_COLUMN_NAME),
                        rs.getString(HANDLEDARE_COLUMN_EMAIL),
                        rs.getString(HANDLEDARE_COLUMN_PHONE_NUMBER),
                        rs.getString(HANDLEDARE_COLUMN_HASHED_PASSWORD)
                ));
            }
            return handledare;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }
    @Override
    public StudentData getStudentData(String email) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            String sql = "SELECT * FROM " + STUDENT_DATA_TABLE + " WHERE email = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                return null;
            }
            String name = rs.getString(STUDENT_DATA_COLUMN_NAME);
            String phoneNumber = rs.getString(STUDENT_DATA_COLUMN_PHONENUMBER);
            String dob = rs.getString(STUDENT_DATA_COLUMN_DOB);
            return new StudentData(email, name, dob, phoneNumber);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public List<String> getAllStudentDataEmails() throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT " + STUDENT_DATA_COLUMN_EMAIL + " FROM " + STUDENT_DATA_TABLE
            );
            List<String> allStudentDataEmails = new ArrayList<>();
            while (rs.next()) {
                allStudentDataEmails.add(rs.getString(STUDENT_DATA_COLUMN_EMAIL));
            }
            return allStudentDataEmails;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public Student getStudent(String email) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            String sql = "SELECT * FROM " + STUDENT_TABLE + " WHERE email = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                return null;
            }
            String personalLetter = rs.getString(STUDENT_COLUMN_PERSONAL_LETTER);
            String region = rs.getString(STUDENT_COLUMN_REGION);
            int choice_1 = rs.getInt(STUDENT_COLUMN_CHOICE_1);
            int choice_2 = rs.getInt(STUDENT_COLUMN_CHOICE_2);
            int choice_3 = rs.getInt(STUDENT_COLUMN_CHOICE_3);
            int choice_4 = rs.getInt(STUDENT_COLUMN_CHOICE_4);
            int choice_5 = rs.getInt(STUDENT_COLUMN_CHOICE_5);
            return new Student(
                    email,
                    getStudentData(email),
                    new Region(region),
                    personalLetter,
                    getUnit(choice_1), getUnit(choice_2), getUnit(choice_3), getUnit(choice_4), getUnit(choice_5)
            );
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public Handledare getHandledare(String email) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            String sql = "SELECT * FROM " + HANDLEDARE_TABLE + " WHERE email = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                return null;
            }
            String phoneNumber = rs.getString(HANDLEDARE_COLUMN_PHONE_NUMBER);
            String hashedPassword = rs.getString(HANDLEDARE_COLUMN_HASHED_PASSWORD);
            String name = rs.getString(HANDLEDARE_COLUMN_NAME);
            if(phoneNumber == null && name == null){
                return new Handledare(email,hashedPassword);
            }else{
                return new Handledare(name, email, phoneNumber, hashedPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public VFUSamordnare getVFUSamordnare(String email) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            String sql = "SELECT * FROM " + VFU_SAM_TABLE + " WHERE email = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                return null;
            }
            String phoneNumber = rs.getString(VFU_COLUMN_PHONE_NUMBER);
            String hashedPassword = rs.getString(VFU_COLUMN_HASHED_PASSWORD);
            String name = rs.getString(VFU_COLUMN_NAME);
            String region = rs.getString(VFU_COLUMN_REGION);
            return new VFUSamordnare(name, email, phoneNumber, new Region(region), hashedPassword);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public List<VFUSamordnare> getAllVFUSamordnare() throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            List<VFUSamordnare> vfuSamordnare = new ArrayList<>();
            Statement statement = connection.createStatement();
            String sql = "SELECT "
                    + VFU_COLUMN_EMAIL + ","
                    + VFU_COLUMN_NAME + ","
                    + VFU_COLUMN_PHONE_NUMBER + ","
                    + VFU_COLUMN_REGION + ","
                    + VFU_COLUMN_HASHED_PASSWORD
                    + " FROM " + VFU_SAM_TABLE
                    + " ORDER BY " + VFU_COLUMN_REGION + "," + VFU_COLUMN_NAME + " COLLATE NOCASE";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String hiddenHashedPassword = "x";
                String hashedPassword = rs.getString(VFU_COLUMN_HASHED_PASSWORD);
                if (hashedPassword == null || hashedPassword.equals("")) {
                    hiddenHashedPassword = null;
                }
                vfuSamordnare.add(new VFUSamordnare(
                        rs.getString(VFU_COLUMN_NAME),
                        rs.getString(VFU_COLUMN_EMAIL),
                        rs.getString(VFU_COLUMN_PHONE_NUMBER),
                        new Region(rs.getString(VFU_COLUMN_REGION)),
                        hiddenHashedPassword
                ));
            }
            return vfuSamordnare;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public Unit getUnit(int id) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            String sql = "SELECT * FROM " + UNIT_TABLE + " WHERE id = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            String name = rs.getString(UNIT_COLUMN_NAME);
            String muni = rs.getString(UNIT_COLUMN_MUNICIPALITY);
            int numOfSlots = rs.getInt(UNIT_COLUMN_NUM_OF_SLOTS);
            String info = rs.getString(UNIT_COLUMN_INFO);
            return new Unit(id, getMunicipality(muni), name, numOfSlots,info);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }

    }
    @Override
    public Place getPlaceViaHandledare(String hand) throws DatabaseException{
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            String sql = "SELECT * FROM " + PLACE_TABLE + " WHERE handledare = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, hand);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                return null;
            }
            int id = rs.getInt(PLACE_COLUMN_ID);
            String student = rs.getString(PLACE_COLUMN_STUDENT);
            int unit = rs.getInt(PLACE_COLUMN_UNIT);

            Place place = new Place(id,getHandledare(hand),getUnit(unit));

            if(student == null){
                return place;
            }else{
                place.setStudent(getStudent(student));
                return place;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public Place getPlace(int id) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            String sql = "SELECT * FROM " + PLACE_TABLE + " WHERE id = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            String handledare = rs.getString(PLACE_COLUMN_HANDLEDARE);
            String unit = rs.getString(PLACE_COLUMN_UNIT);
            String student = rs.getString(PLACE_COLUMN_STUDENT);
            if (handledare == null && student == null) {
                return new Place(id, getUnit(Integer.valueOf(unit)));
            }else if(handledare == null){
                return new Place(id, getStudent(student), getUnit(Integer.valueOf(unit)));
            }else if(handledare != null && student == null){
                return new Place(id, getHandledare(handledare), getUnit(Integer.valueOf(unit)));
            }
            else {
                return new Place(id, getHandledare(handledare), getStudent(student), getUnit(Integer.valueOf(unit)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }


    @Override
    public Unit getUnit(Municipality muni, String name) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            String sql = "SELECT * FROM " + UNIT_TABLE + " WHERE municipality = ? and name = ?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, muni.getName());
            statement.setString(2, name);
            ResultSet rs = statement.executeQuery();

            int id = rs.getInt(UNIT_COLUMN_ID);
            int numOfSlots = rs.getInt(UNIT_COLUMN_NUM_OF_SLOTS);
            String info = rs.getString(UNIT_COLUMN_INFO);
            return new Unit(id, muni, name, numOfSlots,info);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public List<Student> getAllStudents() throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            List<Student> students = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM " + STUDENT_TABLE);
            while (rs.next()) {
                students.add(new Student(
                        rs.getString(STUDENT_COLUMN_EMAIL),
                        getStudentData(rs.getString(STUDENT_COLUMN_EMAIL)),
                        new Region(rs.getString(STUDENT_COLUMN_REGION)),
                        rs.getString(STUDENT_COLUMN_PERSONAL_LETTER),
                        getUnit(rs.getInt(STUDENT_COLUMN_CHOICE_1)),
                        getUnit(rs.getInt(STUDENT_COLUMN_CHOICE_2)),
                        getUnit(rs.getInt(STUDENT_COLUMN_CHOICE_3)),
                        getUnit(rs.getInt(STUDENT_COLUMN_CHOICE_4)),
                        getUnit(rs.getInt(STUDENT_COLUMN_CHOICE_5)))
                );
            }
            return students;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public List<StudentData> getAllStudentData() throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            List<StudentData> allStudentData = new ArrayList<>();
            Statement statement = connection.createStatement();
            String sql = "SELECT " + STUDENT_DATA_COLUMN_EMAIL + ", "
                    + STUDENT_DATA_COLUMN_NAME + ","
                    + STUDENT_DATA_COLUMN_DOB + ","
                    + STUDENT_DATA_COLUMN_PHONENUMBER
                    + " FROM " + STUDENT_DATA_TABLE
                    + " ORDER BY " + STUDENT_DATA_COLUMN_NAME;
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                allStudentData.add(new StudentData(
                        rs.getString(STUDENT_DATA_COLUMN_EMAIL),
                        rs.getString(STUDENT_DATA_COLUMN_NAME),
                        rs.getString(STUDENT_DATA_COLUMN_DOB),
                        rs.getString(STUDENT_DATA_COLUMN_PHONENUMBER)
                ));
            }
            return allStudentData;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public String getVfuEmailFromRegLink(String regLink) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl)) {
            String sql = "SELECT " + LINK_MAIL_VFU_COLUMN_EMAIL + " FROM " + LINK_MAIL_VFU_TABLE
                    + " WHERE " + LINK_MAIL_VFU_COLUMN_REG_LINK + "=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, regLink);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                return null;
            }
            String email = rs.getString(LINK_MAIL_VFU_COLUMN_EMAIL);
            return email;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public Map<String, String> getAllVfuRegLinks() throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl)) {
            String sql = "SELECT * FROM " + LINK_MAIL_VFU_TABLE;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            Map<String, String> vfuRegLinks = new HashMap<>();
            while (rs.next()) {
                vfuRegLinks.put(
                        rs.getString(LINK_MAIL_VFU_COLUMN_EMAIL),
                        rs.getString(LINK_MAIL_VFU_COLUMN_REG_LINK)
                );
            }
            return vfuRegLinks;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public boolean isSocionom(String studentID) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            String sql = "SELECT * FROM " + STUDENT_DATA_TABLE + " WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, studentID + "@student.hig.se");
            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public Municipality getMunicipality(String name) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            String sql = "SELECT * FROM " + MUNI_TABLE + " WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();

            String region = rs.getString(MUNI_COLUMN_REGION);
            return new Municipality(name, new Region(region));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public List<String> getAllMunicipalitiesForRegion(String regionName) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            String sql = "SELECT * FROM " + MUNI_TABLE
                    + " WHERE " + MUNI_COLUMN_REGION + "=?"
                    + " ORDER BY " + MUNI_COLUMN_NAME;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, regionName);

            ResultSet rs = statement.executeQuery();
            List<String> allMunis = new ArrayList<>();
            while (rs.next()) {
                allMunis.add(rs.getString(MUNI_COLUMN_NAME));
            }

            return allMunis;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public List<Unit> getAllUnitFromRegion(String region) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl)) {
            String sql = "SELECT * " + " FROM " + UNIT_TABLE + "," + MUNI_TABLE
                    + " WHERE  municipality.region = ? and unit.municipality = municipality.name and unit.name != 'no_choice'";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, region);

            ResultSet rs = statement.executeQuery();
            List<Unit> allUnits = new ArrayList<>();
            while (rs.next()) {
                int numberOfSlots = rs.getInt(UNIT_COLUMN_NUM_OF_SLOTS);
                String municipality = rs.getString(UNIT_COLUMN_MUNICIPALITY);
                String name = rs.getString(UNIT_COLUMN_NAME);
                int id = rs.getInt(UNIT_COLUMN_ID);
                String info = rs.getString(UNIT_COLUMN_INFO);
                allUnits.add(new Unit(id,getMunicipality(municipality),name,numberOfSlots,info));
            }

            return allUnits;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public List<Unit> getAllUnitFromRegionForStudentChoice(String region) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl)) {
            String sql = "SELECT "
                    + UNIT_COLUMN_ID + ","
                    + UNIT_COLUMN_INFO + ","
                    + UNIT_TABLE + "." + UNIT_COLUMN_NAME + " AS unitName,"
                    + MUNI_TABLE + "." + MUNI_COLUMN_NAME + " AS muniName"
                    + " FROM " + UNIT_TABLE + "," + MUNI_TABLE
                    + " WHERE " + MUNI_TABLE + "." + MUNI_COLUMN_REGION + "=? AND "
                    + UNIT_TABLE + "." + UNIT_COLUMN_MUNICIPALITY + "=" + MUNI_TABLE + "." + MUNI_COLUMN_NAME;

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, region);

            ResultSet rs = statement.executeQuery();

            List<Unit> allUnitsForStudentChoice = new ArrayList<>();
            while (rs.next()) {
                int unitId = rs.getInt(UNIT_COLUMN_ID);
                String info = rs.getString(UNIT_COLUMN_INFO);
                String unitName = rs.getString("unitName");
                String muniName = rs.getString("muniName");
                allUnitsForStudentChoice.add(new Unit(unitId, info, new Municipality(muniName), unitName));
            }

            return allUnitsForStudentChoice;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }
    @Override
    public List<Unit> getAllUnits() throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl)) {
            String sql = "SELECT * " + " FROM " + UNIT_TABLE
                    + " WHERE unit.name != 'no_choice' ORDER BY unit.name COLLATE NOCASE";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            List<Unit> allUnits = new ArrayList<>();

            while (rs.next()) {
                int numberOfSlots = rs.getInt(UNIT_COLUMN_NUM_OF_SLOTS);
                String municipality = rs.getString(UNIT_COLUMN_MUNICIPALITY);
                String name = rs.getString(UNIT_COLUMN_NAME);
                int id = rs.getInt(UNIT_COLUMN_ID);
                String info = rs.getString(UNIT_COLUMN_INFO);
                allUnits.add(new Unit(id,getMunicipality(municipality),name,numberOfSlots,info));
            }

            return allUnits;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public List<Place> getAllPlacesForUnit(Unit unit) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl)) {
            String sql = "SELECT * " + " FROM " + PLACE_TABLE
                    + " WHERE  unit = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, unit.getId());

            ResultSet rs = statement.executeQuery();


            List<Place> allPlaces = new ArrayList<>();
            while (rs.next()) {
                String student = rs.getString(PLACE_COLUMN_STUDENT);
                String handledare = rs.getString(PLACE_COLUMN_HANDLEDARE);
                if(student == null) {
                    int id = rs.getInt(PLACE_COLUMN_ID);
                    allPlaces.add(new Place(id, unit));
                }else if(handledare == null){
                    int id = rs.getInt(PLACE_COLUMN_ID);
                    allPlaces.add(new Place(id, getStudent(student), unit));

                }

            }

            return allPlaces;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }
    @Override
    public boolean isChosen(int id) throws DatabaseException{
        try (Connection connection = DriverManager.getConnection(dbUrl)) {
            String sql = "SELECT * FROM " + STUDENT_TABLE + " WHERE choice_1 = ? or choice_2 = ? or choice_3 = ? or choice_4 = ? or choice_5 = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);
            statement.setInt(2, id);
            statement.setInt(3, id);
            statement.setInt(4, id);
            statement.setInt(5, id);

            ResultSet rs = statement.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }


    @Override
    public Region getRegionFromVfuSamEmail(String email) throws DatabaseException{
        try (Connection connection = DriverManager.getConnection(dbUrl)) {
            String sql = "SELECT * FROM " + VFU_SAM_TABLE + " WHERE email = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            String region = rs.getString(VFU_COLUMN_REGION);

            return new Region(region);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public boolean municipalityExists(String muniName) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            String sql = "SELECT * FROM " + MUNI_TABLE + " WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, muniName);
            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }
    @Override
    public boolean handledareExistsOnPlace(String handledareEmail) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            String sql = "SELECT * FROM " + PLACE_TABLE + " WHERE handledare = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, handledareEmail);
            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public boolean regionExists(String name) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            String sql = "SELECT * FROM " + REGION_TABLE + " WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }
    @Override
    public Place getPlaceViaStudent(String studentEmail) throws DatabaseException{
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            String sql = "SELECT * FROM " + PLACE_TABLE + " WHERE student = ? ";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, studentEmail);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                return null;
            }
            int id = rs.getInt(PLACE_COLUMN_ID);
            String hand = rs.getString(PLACE_COLUMN_HANDLEDARE);
            int unit = rs.getInt(PLACE_COLUMN_UNIT);

            Place place = new Place(id,getStudent(studentEmail),getUnit(unit));

            if(hand == null){
                return place;
            }else{
                place.setHandledare(getHandledare(hand));
                return place;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }

    }

    @Override
    public List<String> getAllRegionNames() throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT " + REGION_COLUMN_NAME
                    + " FROM " + REGION_TABLE
                    + " WHERE " + REGION_COLUMN_NAME + "!='no_region'"
                    + " ORDER BY " + REGION_COLUMN_NAME
            );
            List<String> allRegions = new ArrayList<>();
            while (rs.next()) {
                allRegions.add(rs.getString(REGION_COLUMN_NAME));
            }
            return allRegions;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }

    @Override
    public List<Place> getAllPlaces() throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, sqLiteConfig)) {
            List<Place> places = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM " + PLACE_TABLE );

            while (rs.next()) {
                int id = rs.getInt(PLACE_COLUMN_ID);
                int unit = rs.getInt(PLACE_COLUMN_UNIT);
                String student = rs.getString(PLACE_COLUMN_STUDENT);
                String handledare = rs.getString(PLACE_COLUMN_HANDLEDARE);

                if(handledare == null && student == null){
                    places.add(new Place(id,getUnit(unit),null, null));
                }else if(handledare == null){
                    places.add(new Place(id, getStudent(student), getUnit(unit)));
                }else{
                    places.add(new Place(id, getUnit(unit),getStudent(student),getHandledare(handledare)));
                }

            }
            return places;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("database error", e);
        }
    }
}