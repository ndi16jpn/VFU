package util;

/**
 * Alla sökvägar för webben, samt alla template filssökvägar
 */
public class Path {

    public static class Web {
        public static final String HOME = "/";
        public static final String CONTACT = "/contact/";
        public static final String LOGIN = "/login/";
        public static final String LOGOUT = "/logout/";

        public static final String ADMIN_HOME = "/admin/";
        public static final String ADMIN_DELETE_ALL = "/admin/delete-all/";
        public static final String ADMIN_ADD_STUDENTS = "/admin/add-students/";
        public static final String ADMIN_ADD_STUDENTS_UPLOAD_CSV = "/admin/add-students/upload-csv/";
        public static final String ADMIN_ADD_STUDENTS_UPLOAD_CSV_RESULT = "/admin/add-students/upload-csv/result/";
        public static final String ADMIN_ADD_STUDENTS_SINGLE = "/admin/add-students/add-single/";
        public static final String ADMIN_HANDLE_ADD_SINGLE_PLACE = "/admin/add-one-place/";
        public static final String ADMIN_HANDLE_DELETE_SINGLE_PLACE = "/admin/delete-one-place/";
        public static final String ADMIN_HANDLE_UPDATE_PHONENUMBER = "/admin/update-phonenumber/";
        public static final String ADMIN_HANDLE_REGIONS = "/admin/handle-regions/";
        public static final String ADMIN_HANDLE_REGIONS_ADD_REGION = "/admin/handle-regions/add-region/";
        public static final String ADMIN_HANDLE_REGIONS_ADD_REGION_AJAX = "/admin/handle-regions/add-region-ajax/";
        public static final String ADMIN_HANDLE_REGIONS_ADD_MUNI = "/admin/handle-regions/add-muni/:regionName/";
        public static final String ADMIN_HANDLE_REGIONS_ADD_MUNI_AJAX = "/admin/handle-regions/add-muni-ajax/:regionName/";
        public static final String ADMIN_HANDLE_REGIONS_DELETE_REGION = "/admin/handle-regions/delete-region/";
        public static final String ADMIN_HANDLE_REGIONS_DELETE_REGION_AJAX = "/admin/handle-regions/delete-region-ajax/";
        public static final String ADMIN_HANDLE_REGIONS_DELETE_MUNI = "/admin/handle-regions/delete-muni/";
        public static final String ADMIN_HANDLE_REGIONS_DELETE_MUNI_AJAX = "/admin/handle-regions/delete-muni-ajax/";
        public static final String ADMIN_ADD_PLACE = "/admin/add-place/";
        public static final String ADMIN_ADD_PLACE_SINGLE = "/admin/add-place/";
        public static final String ADMIN_ADD_HANDLEDARE = "/admin/add-handledare/";
        public static final String ADMIN_DELETE_PLACE_SINGLE = "/admin/delete-unit/";
        public static final String ADMIN_DELETE_HANDLEDARE_SINGLE = "/admin/delete-handledare/";
        public static final String ADMIN_SHOW_STUDENTS = "/admin/show-students/";
        public static final String ADMIN_DELETE_STUDENT = "/admin/delete-student/";
        public static final String ADMIN_DELETE_STUDENT_FROM_PLACE = "/admin/delete-student-from-place/";
        public static final String ADMIN_MATCH = "/admin/match/";
        public static final String ADMIN_DO_MATCH = "/admin/do-match/";
        public static final String ADMIN_MATCH_VERIFY_RESULT = "/admin/match/verify-result/";
        public static final String ADMIN_MANUAL_MATCH = "/admin/manual-match-student/";
        public static final String ADMIN_HANDLE_SEND_EMAIL_MATCHED_STUDENTS = "/admin/send-email-matched/";
        public static final String ADMIN_ADD_VFU_SAM = "/admin/add-vfusam/";
        public static final String ADMIN_SHOW_VFU_SAM = "/admin/show-vfusam/";
        public static final String ADMIN_DELETE_VFU_SAM = "/admin/delete-vfusam/";
        public static final String ADMIN_SEND_EMAILS = "/admin/send_handledare_reg_mail/";

        public static final String ADMIN_SHOW_EDIT_STUDENT_MAIN = "/admin/edit/show_edit_student_main/";
        public static final String ADMIN_EDIT_STUDENT_FIRST_TEXT = "/admin/edit-studentFirstText/";

        public static final String STUDENT_HOME = "/student/";
        public static final String STUDENT_CONFIRM_LETTER = "/logged_in/student/confirm_letter/";
        public static final String STUDENT_APPLY_FIRST = "/student/apply-first/";
        public static final String STUDENT_APPLY_CHOICE = "/student/apply-choice/";

        public static final String HANDLEDARE_HOME = "/handledare/";
        public static final String HANDLEDARE_CRITERIA = "/handledare/evaluation-criteria/";

        public static final String VFU_SAMORDNARE_HOME = "/vfu-samordnare/";
        public static final String VFU_SAMORDNARE_UPDATE_PHONENUMBER = "/vfu-samordnare/update-phonenumber/";
        public static final String VFU_SAMORDNARE_ADD_PLACE = "/vfu-samordnare/add-place/";
        public static final String VFU_SAMORDNARE_ADD_PLACE_SINGLE = "/vfu-samordnare/add-place/";
        public static final String VFU_SAMORDNARE_DELETE_PLACE_SINGLE = "/vfu-samordnare/delete-unit/";
        public static final String VFU_SAMORDNARE_DELETE_HANDLEDARE_SINGLE = "/vfu-samordnare/delete-handledare/";
        public static final String VFU_SAMORDNARE_ADD_HANDLEDARE = "/vfu-samordnare/add-handledare/";
        public static final String VFU_SAMORDNARE_HANDLE_ADD_SINGLE_PLACE = "/vfu-samordnare/add-one-place/";
        public static final String VFU_SAMORDNARE_HANDLE_DELETE_SINGLE_PLACE = "/vfu-samordnare/delete-one-place/";

        public static final String REG_HANDLEDARE = "/reg/handledare/:regLink/";
        public static final String REG_VFU_SAMORDNARE = "/reg/vfu-samordnare/:regLink/";

    }

    public static class Template {
        public static final String INDEX = "logged_out/index";
        public static final String CONTACT = "logged_out/contact";
        public static final String LOGIN = "logged_out/login";
        public static final String ERROR = "logged_out/error";

        public static final String ADMIN_HOME = "logged_in/admin/admin";
        public static final String ADMIN_DELETE_ALL = "logged_in/admin/delete/delete_all";
        public static final String ADMIN_ERROR = "logged_in/admin/error";
        public static final String ADMIN_ADD_STUDENTS = "logged_in/admin/add_students/add_students";
        public static final String ADMIN_ADD_STUDENTS_VERIFY = "logged_in/admin/add_students/verify";
        public static final String ADMIN_ADD_STUDENTS_VERIFY_RESULT = "logged_in/admin/add_students/verify_result";
        public static final String ADMIN_ADD_STUDENTS_SINGLE_VERIFY_RESULT = "logged_in/admin/add_students/single_verify_result";
        public static final String ADMIN_HANDLE_REGIONS = "logged_in/admin/handle_regions/handle_regions";
        public static final String ADMIN_ADD_PLACE = "logged_in/admin/add_place/add_place";
        public static final String ADMIN_SHOW_STUDENTS = "logged_in/admin/show_students/show_students";
        public static final String ADMIN_MATCH = "logged_in/admin/match/match";
        public static final String ADMIN_MATCH_VERIFY = "logged_in/admin/match/match_verify";
        public static final String ADMIN_ADD_VFU_SAM = "logged_in/admin/add_vfu_sam/add_vfu_sam";
        public static final String ADMIN_SHOW_VFU_SAM = "logged_in/admin/show_vfu_sam/show_vfu_sam";
        public static final String ADMIN_SHOW_EDIT_STUDENT_MAIN = "logged_in/admin/edit/show_edit_student_main";
        public static final String ADMIN_SEND_EMAILS = "logged_in/admin/send_handledare_reg_mail";

        public static final String STUDENT_FIRST = "logged_in/student/first/first";
        public static final String STUDENT_CHOICE = "logged_in/student/choice/choice";
        public static final String STUDENT_STATUS = "logged_in/student/status/status";
        public static final String STUDENT_ERROR = "logged_in/student/error";


        public static final String HANDLEDARE_HOME = "logged_in/handledare/handledare";
        public static final String HANDLEDAER_CRITERIA = "logged_in/handledare/evaluation_criteria/evaluation_criteria";
        public static final String HANDLEDARE_ERROR = "logged_in/handledare/error";

        public static final String VFU_SAMORDNARE_HOME = "logged_in/vfu_samordnare/vfu_samordnare";
        public static final String VFU_SAMORDNARE_ERROR = "logged_in/vfu_samordnare/error";
        public static final String VFU_SAMORDNARE_ADD_PLACE  = "logged_in/vfu_samordnare/add_place/add_place";

        public static final String REG_HANDLEDARE  = "logged_out/registration/regmail_handledare";
        public static final String REG_VFU_SAMORDNARE  = "logged_out/registration/regmail_vfusam";
        public static final String REG_INVALID_LINK  = "logged_out/registration/invalid_link";

        public static final String EMAIL_REG_VFU_SAMORDNARE  = "email/vfu_reg";
        public static final String EMAIL_STUDENT_ADDED  = "email/student_added";
        public static final String EMAIL_STUDENT_MATCHED  = "email/student_matched";

    }

}
