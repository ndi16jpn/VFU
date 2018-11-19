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

import java.util.List;

public interface DatabaseInserter {
    /**
     * Adds students from a list of studentdata to the database
     * @param studentData
     * @throws DatabaseException
     */
    void addStudentsViaCSV(List<StudentData> studentData) throws DatabaseException;

    /**
     * Adds a single student to the database
     * @param student
     * @throws DatabaseException
     */
    void addSingleStudent(StudentData student) throws DatabaseException;

    /**
     * Adds the student firstpage when students email, students region and students personal letter is input
     * @param email
     * @param region
     * @param personalLetter
     * @throws DatabaseException
     */
    void addStudentFirstPage(String email, Region region, String personalLetter) throws DatabaseException;

    /**
     * Sets the student belonging to the email's choices
     * @param email
     * @param choices
     * @throws DatabaseException
     */
    void setStudentChoices(String email, int[] choices) throws DatabaseException;

    /**
     * Adds the specified VFUsamordnare to the database
     * @param samordnare
     * @throws DatabaseException
     */
    void addVFUSamordnare(VFUSamordnare samordnare) throws DatabaseException;

    /**
     * Adds the specified Handledare to the database
     * @param handledare
     * @throws DatabaseException
     */
    void addHandledare(Handledare handledare) throws DatabaseException;

    /**
     * Adds the registration link to the handledare to the database
     * @param linkMailHandledare
     * @throws DatabaseException
     */
    void addLinkMailHandledare(LinkMailHandledare linkMailHandledare) throws DatabaseException;

    /**
     * Adds the registration link to the VFUsamordnare to the database
     * @param linkMailVFUSamordnare
     * @throws DatabaseException
     */
    void addLinkMailVFU(LinkMailVFUSamordnare linkMailVFUSamordnare) throws DatabaseException;

    /**
     * Adds the specified region to the database
     * @param region
     * @throws DatabaseException
     */
    void addRegion(Region region) throws DatabaseException;

    /**
     * Adds the specified municipality to the database
     * @param municipality
     * @throws DatabaseException
     */
    void addMunicipality(Municipality municipality) throws DatabaseException;

    /**
     * Adds the specified unit to the database
     * Returns the id of the unit
     * @param unit
     * @return
     * @throws DatabaseException
     */
    long addUnit(Unit unit) throws DatabaseException;

    /**
     * Adds the specified place to the database
     * @param place
     * @throws DatabaseException
     */
    void addPlace(Place place) throws DatabaseException;
    /**
     * Adds a single place to the database
     * @param place
     * @throws DatabaseException
     */
    void addSinglePlace(Place place) throws DatabaseException;
    /**
     * Deletes one on number of slots
     * @param unit
     * @throws DatabaseException
     */
    void deleteOneNumberOfSlotsUnit(Unit unit) throws DatabaseException;

    void updatePhonenumberHandledare(String phoneNumber, String handledareEmail) throws DatabaseException;

    /**
     * Adds to specified place the specified handledare
     * @param place
     * @param handledare
     * @throws DatabaseException
     */


    void addHandledareToPlace(Place place, Handledare handledare) throws DatabaseException;
    /**
     * Updates the number of slots on a unit
     * @param unit
     * @throws DatabaseException
     */
    void addOneNumberOfSlotsUnit(Unit unit) throws DatabaseException;
    /**
     * Adds to specified place the specified student
     * @param place
     * @param student
     * @throws DatabaseException
     */
    void addStudentToPlace(Place place, Student student) throws DatabaseException;

    /**
     * Adds to specified place ID the student with specified email address
     * @param placeId
     * @param studentEmail
     * @throws DatabaseException
     */
    void addStudentToPlace(int placeId, String studentEmail) throws DatabaseException;

    /**
     * Updates the students phone number to this new string
     * @param phoneNumber
     * @param email
     * @throws DatabaseException
     */
    void updatePhoneNumberStudent(String phoneNumber, String email) throws DatabaseException;

    /**
     * Sets the VFUsamordnare with this email's new password to this hashed string
     * @param email
     * @param hashedPassword
     * @throws DatabaseException
     */
    void setVfuSamordnarePassword(String email, String hashedPassword) throws DatabaseException;

}
