package database;

import organisations.Place;
import organisations.Region;
import organisations.Unit;
import roles.Handledare;
import roles.Student;

import java.util.List;

public interface DatabaseDeleter {
    /**
     * Deletes all place content
     * @throws DatabaseException
     */
    void deleteAllPlaceContent() throws DatabaseException;
    /**
     * Deletes all region content
     * @throws DatabaseException
     */
    void deleteAllRegionContent() throws DatabaseException;
    /**
     * Deletes a single place to the database
     * @param place
     * @throws DatabaseException
     */
    void deleteSinglePlace(Place place) throws DatabaseException;
    /**
     * Deletes all municipality content
     * @throws DatabaseException
     */
    void deleteAllMuniContent() throws DatabaseException;
    /**
     * Deletes all unit content
     * @throws DatabaseException
     */
    void deleteAllUnitContent() throws DatabaseException;
    /**
     * Deletes all matchable student content
     * @throws DatabaseException
     */
    void deleteAllStudentContent() throws DatabaseException;
    /**
     * Deletes all content related to VFU-samordnare
     * @throws DatabaseException
     */
    void deleteAllVFUSamContent() throws DatabaseException;
    /**
     * Deletes all student data content
     * @throws DatabaseException
     */
    void deleteAllStudentDataContent() throws DatabaseException;
    /**
     * Deletes all handledare content
     * @throws DatabaseException
     */
    void deleteAllHandledareContent() throws DatabaseException;

    /**
     * Deletes specified place content
     * @param place
     * @throws DatabaseException
     */
    void deletePlaceContent(Place place) throws DatabaseException;

    /**
     * Deletes all places for specified Unit
     * @param unit
     * @throws DatabaseException
     */
    void deleteAllPlacesForUnit(Unit unit) throws DatabaseException;

    /**
     * Deletes all municipalities that belongs to and the specified Region
     * @param region
     * @throws DatabaseException
     */
    void deleteRegionAndMuniContent(Region region) throws DatabaseException;

    /**
     * Deletes content from the municipality with specified name
     * @param municipality
     * @param region
     * @throws DatabaseException
     */
    void deleteMuniContent(String municipality, String region) throws DatabaseException;

    /**
     * Deletes content of specified Unit
     * @param unit
     * @throws DatabaseException
     */
    void deleteUnitContent(Unit unit) throws DatabaseException;

    /**
     * Deletes the content of the student with the specified email address
     * @param studentEmail
     * @throws DatabaseException
     */
    void deleteStudentContent(String studentEmail) throws DatabaseException;

    /**
     * Deletes the content related to the VFU-samordnare with the specified email address
     * @param email
     * @throws DatabaseException
     */
    void deleteVFUSamContent(String email) throws DatabaseException;

    /**
     * Deletes the student data belonging to the specified email address
     * @param studentEmail
     * @throws DatabaseException
     */
    void deleteStudentDataContent(String studentEmail) throws DatabaseException;

    /**
     * Deletes the content related to specified Handledare
     * @param handledare
     * @throws DatabaseException
     */
    void deleteHandledareContent(Handledare handledare) throws DatabaseException;

    /**
     * Removes the specified Handledare from its assigned place
     * @param handledare
     * @throws DatabaseException
     */
    void deleteHandledareFromPlace(Handledare handledare) throws DatabaseException;

    /**
     * Removes the student related to specified email address from its assigned place
     * @param studentEmail
     * @throws DatabaseException
     */
    void deleteStudentFromPlace(String studentEmail) throws DatabaseException;

    /**
     * Deletes the related content to the registration link provided
     * @param regLink
     * @throws DatabaseException
     */
    void deleteLinkMailVFUContent(String regLink) throws DatabaseException;

    /**
     * Deletes all students from their assigned places
     * @param students
     * @throws DatabaseException
     */
    void deleteAllStudentFromPlaceTable(List<Student> students) throws DatabaseException;

    /**
     * Deletes all handledare from their assigned places
     * @param handledares
     * @throws DatabaseException
     */
    void deleteAllHandledareFromPlaceTable(List<Handledare> handledares) throws DatabaseException;

    /**
     * Deletes  the related content to the registration link provided
     * @param regLink
     * @throws DatabaseException
     */
    void deleteLinkMailHandledareContent(String regLink) throws DatabaseException;


}
