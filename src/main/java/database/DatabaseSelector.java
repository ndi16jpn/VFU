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

import java.util.List;
import java.util.Map;

public interface DatabaseSelector {

    /**
     * Gets the admin
     * @return
     * @throws DatabaseException
     */
    Admin getAdmin() throws DatabaseException;

    /**
     * Gets the student data for the student with the specified email address
     * @param email
     * @return
     * @throws DatabaseException
     */
    StudentData getStudentData(String email) throws DatabaseException;

    /**
     * Gets a list of all students email addresses
     * @return
     * @throws DatabaseException
     */
    List<String> getAllStudentDataEmails() throws DatabaseException;

    /**
     * Gets the student with the specified email address
     * @param email
     * @return
     * @throws DatabaseException
     */
    Student getStudent(String email) throws DatabaseException;

    /**
     * Gets the handledare with the specified email address
     * @param email
     * @return
     * @throws DatabaseException
     */
    Handledare getHandledare(String email) throws DatabaseException;

    /**
     * Gets the VFUsamordnare with the specified email address
     * @param email
     * @return
     * @throws DatabaseException
     */
    VFUSamordnare getVFUSamordnare(String email) throws DatabaseException;

    /**
     * Gets a list of all of the VFUsamordares
     * @return
     * @throws DatabaseException
     */
    List<VFUSamordnare> getAllVFUSamordnare() throws DatabaseException;

    /**
     * Gets a list of all the students
     * @return
     * @throws DatabaseException
     */
    List<Student> getAllStudents() throws DatabaseException;

    /**
     * Gets the list of all the students data
     * @return
     * @throws DatabaseException
     */
    List<StudentData> getAllStudentData() throws DatabaseException;

    /**
     * Gets the VFUsamordnares email from registration link
     * @param regLink
     * @return email address
     * @throws DatabaseException
     */
    String getVfuEmailFromRegLink(String regLink) throws DatabaseException;

    /**
     * Gets all vfu reg links
     * @return all vfu reg links
     * @throws DatabaseException
     */
    Map<String, String> getAllVfuRegLinks() throws DatabaseException;

    /**
     * Gets a list of all places
     * @return
     * @throws DatabaseException
     */
    List<Place> getAllPlaces() throws DatabaseException;

    /**
     * Gets all handledares
     * @return
     * @throws DatabaseException
     */
    List<Handledare> getAllHandledare()throws DatabaseException;

    /**
     * Gets all the places that lack a student entry
     * @return
     * @throws DatabaseException
     */
    List<Place> getAllPlacesWithoutStudent() throws DatabaseException;

    /**
     * Gets all the places that lack a handledare entry
     * @return
     * @throws DatabaseException
     */
    List<Place> getAllPlacesWithStudent() throws DatabaseException;

    /**
     * Gets the unit from specified municipality with the specified name
     * @param muni
     * @param name
     * @return
     * @throws DatabaseException
     */
    Unit getUnit(Municipality muni, String name) throws DatabaseException;

    /**
     * Gets the unit from the specified id
     * @param id
     * @return
     * @throws DatabaseException
     */
    Unit getUnit(int id) throws DatabaseException;

    /**
     * Checks whether the student ID matches a socionom student
     * @param studentID
     * @return
     * @throws DatabaseException
     */
    boolean isSocionom(String studentID) throws DatabaseException;

    /**
     * Gets the municipality that has the specified name
     * @param name
     * @return
     * @throws DatabaseException
     */
    Municipality getMunicipality(String name) throws DatabaseException;

    /**
     * Gets a list of all the municipalities in the region with the specified name
     * @param regionName
     * @return
     * @throws DatabaseException
     */
    List<String> getAllMunicipalitiesForRegion(String regionName) throws DatabaseException;

    /**
     * Checks whether the municipality with the specified name already exists or not
     * @param muniName
     * @return
     * @throws DatabaseException
     */
    boolean municipalityExists(String muniName) throws DatabaseException;
    /**
     * Checks whether the municipality with the specified name already exists or not
     * in the specified region
     * @param muniName
     * @param regionName
     * @return
             * @throws DatabaseException
     */
    boolean municipalityExistsInRegion(String muniName, String regionName) throws DatabaseException;

    /**
     * Checks whether the municipality with the specified name already exists or not
     * in the specified region
     * @param muniName
     * @return
     * @throws DatabaseException
     */
    boolean municipalityHasRegions(String muniName) throws DatabaseException;

    /**
     * Checks whether the region with the specified name already exists or not
     * @param name
     * @return
     * @throws DatabaseException
     */
    boolean regionExists(String name) throws DatabaseException;

    /**
     * Gets a list with the names of all regions in the database
     * @return
     * @throws DatabaseException
     */
    List<String> getAllRegionNames() throws DatabaseException;

    /**
     * Gets the place from specified ID
     * @param id
     * @return
     * @throws DatabaseException
     */
    Place getPlace(int id) throws DatabaseException;

    /**
     * Gets the place that has the specified handledare assigned to it
     * @param hand
     * @return
     * @throws DatabaseException
     */
    Place getPlaceViaHandledare(String hand) throws DatabaseException;

    /**
     * Gets the place that has the specified student that coincides with this student email assigned to it
     * @param studentEmail
     * @return
     * @throws DatabaseException
     */
    Place getPlaceViaStudent(String studentEmail) throws DatabaseException;

    /**
     * Gets the region from the VFUsamordnare with the specified email
     * @param email
     * @return
     * @throws DatabaseException
     */
    Region getRegionFromVfuSamEmail(String email) throws DatabaseException;

    /**
     * Gets all units in the region that coincides with the specified name
     * @param region
     * @return
     * @throws DatabaseException
     */
    List<Unit> getAllUnitFromRegion(String region) throws DatabaseException;

    /**
     * Gets all units in the database
     * @return
     * @throws DatabaseException
     */
    List<Unit> getAllUnits() throws DatabaseException;

    /**
     * Gets all units from the specified region
     * @param region
     * @return
     * @throws DatabaseException
     */
    List<Unit> getAllUnitFromRegionForStudentChoice(String region) throws DatabaseException;

    /**
     * Gets all places from the specified unit
     * @param unit
     * @return
     * @throws DatabaseException
     */
    List<Place> getAllPlacesForUnit(Unit unit) throws DatabaseException;

    /**
     * Checks whether the handledare exists in a place
     * @param handledareEmail
     * @return
     * @throws DatabaseException
     */
    boolean handledareExistsOnPlace(String handledareEmail) throws DatabaseException;

    /**
     * Checks if the specified unit id has been chosen by a student
     * @param id
     * @return
     * @throws DatabaseException
     */
    boolean isChosen(int id) throws DatabaseException;

    /**
     * Gets all supervisors for a place
     * @param placeId
     * @return
     * @throws DatabaseException
     */
    List<Handledare> getHandledareForPlace(int placeId) throws DatabaseException;

    /**
     * Gets all supervisors for a place
     * @param paragraphNr
     * @return
     * @throws DatabaseException
     */
    String getStudentFirstParagraph(int paragraphNr) throws DatabaseException;

    /**
     * Get handledare registration mails
     * @throws DatabaseException
     */
    List<String> getAllHandledareRegistrationMail() throws DatabaseException;
}
