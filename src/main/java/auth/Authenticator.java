package auth;

import data.StudentData;
import database.Database;
import database.DatabaseException;
import database.DatabaseHandler;
import roles.Admin;
import roles.Handledare;
import roles.VFUSamordnare;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static auth.LDAP.authenticateHigLdap;
import static security.PasswordSecurity.validatePassword;

/**
 * Autentiserar lösenord och användarnamn för alla olika roller.
 */
public class Authenticator {

    public static boolean authenticateAdmin(String email, String password) {
        Admin existingAdmin;
        try {
            Database db = DatabaseHandler.getDatabase();
            existingAdmin = db.getSelector().getAdmin();
        } catch (DatabaseException e) {
            e.printStackTrace();
            return false;
        }
        if (!existingAdmin.getEmail().equals(email)) {
            return false;
        }

        try {
            return validatePassword(password.toCharArray(), existingAdmin.getHashedPassword());
        } catch (InvalidKeySpecException|NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean authenticateStudent(String studentId, String password) {
        try {
            Database db = DatabaseHandler.getDatabase();
            if (!db.getSelector().isSocionom(studentId)) {
                return false;
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
            return false;
        }
        StudentData existingStudentData;
        //return authenticateHigLdap(studentId, password);
        try {
            Database db = DatabaseHandler.getDatabase();
            existingStudentData = db.getSelector().getStudentData(studentId);
            return validatePassword(password.toCharArray(), existingStudentData.getHashedPassword());
        } catch (InvalidKeySpecException|NoSuchAlgorithmException| DatabaseException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean authenticateVFUSamordnare(String email, String password) {
        VFUSamordnare existingVFUSam;
        try {
            Database db = DatabaseHandler.getDatabase();
            existingVFUSam = db.getSelector().getVFUSamordnare(email);
            if (existingVFUSam == null || existingVFUSam.getHashedPassword().equals("")) {
                return false;
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
            return false;
        }
        try {
            return validatePassword(password.toCharArray(), existingVFUSam.getHashedPassword());
        } catch (InvalidKeySpecException|NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean authenticateHandledare(String email, String password) {
        Handledare existingHandledare;
        try {
            Database db = DatabaseHandler.getDatabase();
            existingHandledare = db.getSelector().getHandledare(email);
            if (existingHandledare == null) {
                return false;
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
            return false;
        }
        try {
            return validatePassword(password.toCharArray(), existingHandledare.getHashedPassword());
        } catch (InvalidKeySpecException|NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }

}
