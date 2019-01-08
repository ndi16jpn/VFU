package main;

import admin.AdminController;
import contact.ContactController;
import email.MailSenderProvider;
import handledare.HandledareController;
import index.IndexController;
import login.LoginController;
import registration.RegistrationController;
import student.StudentController;
import util.Filters;
import util.Path;
import util.ViewUtil;
import vfu_samordnare.VfuSamordnareController;

import static spark.Spark.*;

/**
 * Main klass som initierar spark samt skapar default smtp.json config fil om den ej finns
 */
public class Main {
    public static void main(String[] args) throws Exception {
        MailSenderProvider.createDefaultConfFileIfNotExists();
        initSpark();
    }

    private static void initSpark() {
        ipAddress("localhost");
        port(4567);
        staticFiles.location("/public");
        staticFiles.expireTime(600);

        before("*", Filters.addTrailingSlashes);
        before("*", Filters.setSessionTimeout);

        get(Path.Web.HOME, IndexController.serveIndexPage);
        get(Path.Web.CONTACT, ContactController.serveContactPage);
        get(Path.Web.LOGIN, LoginController.serveLoginPage);
        post(Path.Web.LOGIN, LoginController.handleLoginPost);
        post(Path.Web.LOGOUT, LoginController.handleLogoutPost);

        get(Path.Web.ADMIN_HOME, AdminController.serveAdminHomePage);
        get(Path.Web.ADMIN_DELETE_ALL, AdminController.serveAdminDeleteAllPage);
        get(Path.Web.ADMIN_ADD_STUDENTS, AdminController.serveAdminAddStudentsPage);
        get(Path.Web.ADMIN_ADD_PLACE, AdminController.serveAdminAddPlacePage);
        post(Path.Web.ADMIN_ADD_STUDENTS_UPLOAD_CSV, AdminController.handleUploadCsvPost);
        post(Path.Web.ADMIN_ADD_STUDENTS_UPLOAD_CSV_RESULT, AdminController.handleUploadCsvResultPost);
        post(Path.Web.ADMIN_ADD_STUDENTS_SINGLE, AdminController.handleAddSingleStudentPost);
        post(Path.Web.ADMIN_DELETE_ALL, AdminController.handleDeleteAllPost);
        get(Path.Web.ADMIN_HANDLE_REGIONS, AdminController.serveAdminHandleRegionsPage);
        post(Path.Web.ADMIN_HANDLE_REGIONS_ADD_REGION, AdminController.handleAddRegionPost);
        post(Path.Web.ADMIN_HANDLE_REGIONS_ADD_REGION_AJAX, AdminController.handleAddRegionPostAjax);
        post(Path.Web.ADMIN_HANDLE_REGIONS_ADD_MUNI, AdminController.handleAddMuniPost);
        post(Path.Web.ADMIN_HANDLE_REGIONS_ADD_MUNI_AJAX, AdminController.handleAddMuniPostAjax);
        post(Path.Web.ADMIN_HANDLE_REGIONS_DELETE_REGION, AdminController.handleDeleteRegionPost);
        post(Path.Web.ADMIN_HANDLE_REGIONS_DELETE_REGION_AJAX, AdminController.handleDeleteRegionPostAjax);
        post(Path.Web.ADMIN_HANDLE_REGIONS_DELETE_MUNI, AdminController.handleDeleteMuniPost);
        post(Path.Web.ADMIN_HANDLE_REGIONS_DELETE_MUNI_AJAX, AdminController.handleDeleteMuniPostAjax);
        post(Path.Web.ADMIN_HANDLE_UPDATE_PHONENUMBER, AdminController.handleUpdatePhoneNumberPost);
        post(Path.Web.ADMIN_ADD_PLACE_SINGLE, AdminController.handelAdminAddSingleUnitPost);
        post(Path.Web.ADMIN_DELETE_PLACE_SINGLE, AdminController.handleAdminDeleteUnitPost);
        post(Path.Web.ADMIN_ADD_HANDLEDARE, AdminController.handleAdminAddHandledarePost);
        post(Path.Web.ADMIN_DELETE_HANDLEDARE_SINGLE, AdminController.handleAdminDeleteHandledarePost);
        post(Path.Web.ADMIN_HANDLE_ADD_SINGLE_PLACE, AdminController.handleAddSinglePlacePost);
        post(Path.Web.ADMIN_HANDLE_DELETE_SINGLE_PLACE, AdminController.handleDeleteSinglePlacePost);
        post(Path.Web.ADMIN_SEND_EMAILS, AdminController.handleSendEmailToHandledarePost);
        post(Path.Web.ADMIN_CHANGE_PLACE_RESERVED_STATUS, AdminController.handleChangePlaceReservedStatus);


        get(Path.Web.ADMIN_SHOW_STUDENTS, AdminController.serveAdminShowStudentsPage);
        post(Path.Web.ADMIN_DELETE_STUDENT, AdminController.handleDeleteStudentPost);
        get(Path.Web.ADMIN_MATCH, AdminController.serveAdminMatchPage);
        post(Path.Web.ADMIN_DO_MATCH, AdminController.handleDoMatchPost);
        post(Path.Web.ADMIN_MATCH_VERIFY_RESULT, AdminController.handleDoMatchVerifyPost);
        post(Path.Web.ADMIN_MANUAL_MATCH, AdminController.handleManualMatchPost);
        post(Path.Web.ADMIN_HANDLE_SEND_EMAIL_MATCHED_STUDENTS, AdminController.handleSendEmailMatchedStudentsPost);
        get(Path.Web.ADMIN_ADD_VFU_SAM, AdminController.serveAdminAddVfuSamPage);
        post(Path.Web.ADMIN_ADD_VFU_SAM, AdminController.handleAddVfuSamPost);
        get(Path.Web.ADMIN_SHOW_VFU_SAM, AdminController.serveAdminShowVfuSamPage);
        post(Path.Web.ADMIN_DELETE_VFU_SAM, AdminController.handleDeleteVfuSamPost);
        post(Path.Web.ADMIN_DELETE_STUDENT_FROM_PLACE, AdminController.handleDeleteStudentFromPlacePost);

        get(Path.Web.ADMIN_SHOW_EDIT_STUDENT_MAIN, AdminController.handleEditStudentFirstPage);
        get(Path.Web.ADMIN_SHOW_EDIT_HANDLEDARE_MAIN, AdminController.handleEditHandledareFirstPage);
        get(Path.Web.ADMIN_SHOW_EDIT_VFU_SAMORDNARE_MAIN, AdminController.handleEditVFUsamordnareFirstPage);

        get(Path.Web.STUDENT_HOME, StudentController.serveStudentHomePage);
        post(Path.Web.STUDENT_APPLY_FIRST, StudentController.handleApplyFirst);
        post(Path.Web.STUDENT_CONFIRM_LETTER,StudentController.handleConfirmLetter);
        post(Path.Web.STUDENT_APPLY_CHOICE, StudentController.handleApplyChoice);

        get(Path.Web.HANDLEDARE_HOME, HandledareController.serveHandledareHomePage);
        get(Path.Web.HANDLEDARE_CRITERIA,HandledareController.serveHandledareCriteriaPage);

        get(Path.Web.VFU_SAMORDNARE_HOME, VfuSamordnareController.serveVfuSamordnareHomePage);
        get(Path.Web.VFU_SAMORDNARE_ADD_PLACE, VfuSamordnareController.serveVfuSamordnareAddPlacePage);
        post(Path.Web.VFU_SAMORDNARE_UPDATE_PHONENUMBER, VfuSamordnareController.handleUpdatePhoneNumberPost);
        post(Path.Web.VFU_SAMORDNARE_ADD_PLACE_SINGLE, VfuSamordnareController.handleVfuSamordnareAddSinglePost);
        post(Path.Web.VFU_SAMORDNARE_DELETE_PLACE_SINGLE, VfuSamordnareController.handleVfuSamordnareDeleteUnitPost);
        post(Path.Web.VFU_SAMORDNARE_ADD_HANDLEDARE, VfuSamordnareController.handleVfuSamordnareAddHandledarePost);
        post(Path.Web.VFU_SAMORDNARE_DELETE_HANDLEDARE_SINGLE, VfuSamordnareController.handleVfuSamordnareDeleteHandledarePost);
        post(Path.Web.VFU_SAMORDNARE_HANDLE_DELETE_SINGLE_PLACE, VfuSamordnareController.handleDeleteSinglePlacePost);
        post(Path.Web.VFU_SAMORDNARE_HANDLE_ADD_SINGLE_PLACE, VfuSamordnareController.handleAddSinglePlacePost);

        get(Path.Web.REG_VFU_SAMORDNARE, RegistrationController.serveRegVfuSam);
        post(Path.Web.REG_VFU_SAMORDNARE, RegistrationController.handleRegVfuSam);

        notFound(ViewUtil.notFound);
        internalServerError(ViewUtil.internalServerError);
    }

}
