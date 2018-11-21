package admin;

import auth.RandomGenerator;
import data.LinkMailVFUSamordnare;
import data.StudentData;
import database.*;
import email.MailSender;
import email.MailSenderProvider;
import json.JsonHandler;
import json.JsonHelper;
import matching.Match;
import matching.MatchStudentPlace;
import organisations.Municipality;
import organisations.Place;
import organisations.Region;
import organisations.Unit;
import roles.Handledare;
import roles.LoggedInRole;
import roles.Student;
import roles.VFUSamordnare;
import security.PasswordSecurity;
import spark.Request;
import spark.Response;
import spark.Route;
import util.CSVParser;
import util.Path;
import util.ViewUtil;

import javax.servlet.MultipartConfigElement;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static login.LoginController.*;
import static util.ViewUtil.render;

/**
 * Hanterar alla requests för admin på sidan
 */
public class AdminController {

    private static final String ATTR_STUDENT_DATA_LIST = "student_data_list";
    private static final String ATTR_ADD_REGION = "add_region";
    private static final String ATTR_ADD_MUNI = "add_muni";
    private static final String ATTR_DELETE_REGION = "delete_region";
    private static final String ATTR_DELETE_MUNI = "delete_muni";
    private final int a = 0;

    public static Route serveAdminHomePage = (Request request, Response response) -> {
        if (isAdmin(request)) {
            Map<String, Object> model = new HashMap<>();
            model.put("page_title", "VFU-portal SOCIONOM");
            model.put("text_title", "Välkommen admin");
            model.put("text_content", "Här kan du som admin göra massor med saker. Använd menyn högst upp.");
            model.put("home_link", Path.Web.ADMIN_HOME);
            model.put(ATTR_ROLE, LoggedInRole.ADMIN.getRoleName());
            model.put(ATTR_NAME, request.session().attribute(ATTR_NAME));
            return render(model, Path.Template.ADMIN_HOME);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route serveAdminDeleteAllPage = (Request request, Response response) -> {
        if (isAdmin(request)) {
            Map<String, Object> model = new HashMap<>();
            model.put("page_title", "VFU-portal SOCIONOM");
            model.put("home_link", Path.Web.ADMIN_HOME);
            model.put(ATTR_ROLE, LoggedInRole.ADMIN.getRoleName());
            model.put(ATTR_NAME, request.session().attribute(ATTR_NAME));
            if(request.attribute("err") != null){
                model.put("cantDelete", "Du måste välja vad du ska ta bort");
            }


            return render(model, Path.Template.ADMIN_DELETE_ALL);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route serveAdminAddPlacePage = (Request request, Response response) -> {
        if (isAdmin(request)) {
            Map<String, Object> model = new HashMap<>();
            model.put("page_title", "VFU-portal SOCIONOM");
            model.put("home_link", Path.Web.ADMIN_HOME);
            model.put(ATTR_ROLE, LoggedInRole.ADMIN.getRoleName());
            model.put(ATTR_NAME, request.session().attribute(ATTR_NAME));
            Database db = DatabaseHandler.getDatabase();
            List<String> regions = db.getSelector().getAllRegionNames();
            model.put("region_list", regions);
            Map regionMuni = new HashMap();
            if(request.attribute("errDel") != null){
                model.put("cantDelete", "Du kan inte ta bort en enhet som har en student eller en enhet som en student har som ett val.");
            }else if(request.attribute("errAdd") != null){
                model.put("cantDelete", "Den här platsen finns redan på den här kommunen");
            }else if(request.attribute("errDelPlaceStud") != null){
                model.put("cantDelete", "Den här platsen har en student");
            }

            for(String region : regions) {
               regionMuni.put(region, db.getSelector().getAllMunicipalitiesForRegion(region));
            }

            model.put("unit_list", db.getSelector().getAllUnits());
            model.put("place_id", db.getSelector().getAllPlaces());
            model.put("region_muni", regionMuni);
            return render(model, Path.Template.ADMIN_ADD_PLACE);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route serveAdminAddStudentsPage = (Request request, Response response) -> {
        if (isAdmin(request)) {
            Map<String, Object> model = new HashMap<>();
            model.put("page_title", "VFU-portal SOCIONOM");
            model.put("home_link", Path.Web.ADMIN_HOME);
            model.put(ATTR_ROLE, LoggedInRole.ADMIN.getRoleName());
            model.put(ATTR_NAME, request.session().attribute(ATTR_NAME));

            return render(model, Path.Template.ADMIN_ADD_STUDENTS);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route handleUploadCsvPost = (Request request, Response response) -> {
        if (isAdmin(request)) {
            request.attribute("org.eclipse.jetty.multipartConfig",
                    new MultipartConfigElement(System.getProperty("java.io.tmpdir")));
            List<StudentData> studentDataList;
            try (InputStream is = request.raw().getPart("students_csv").getInputStream()) {
                List<String> csvLines = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                        .lines().collect(Collectors.toList());
                studentDataList = CSVParser.parseStudentCSV(csvLines);
            }
            Map<String, Object> model = new HashMap<>();
            model.put("page_title", "VFU-portal SOCIONOM");
            model.put("home_link", Path.Web.ADMIN_HOME);
            model.put("student_data_list", studentDataList);
            model.put(ATTR_ROLE, LoggedInRole.ADMIN.getRoleName());
            model.put(ATTR_NAME, request.session().attribute(ATTR_NAME));
            request.session().attribute(ATTR_STUDENT_DATA_LIST, studentDataList);
            return render(model, Path.Template.ADMIN_ADD_STUDENTS_VERIFY);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route handleAdminDeleteUnitPost = (Request request, Response response) -> {
        if (isAdmin(request)) {
            Map<String, Object> model = new HashMap<>();
            String deleteQuery = getQueryUnitToDelete(request);
            String[] parts = deleteQuery.split("@@");
            String name = parts[0];
            String muni = parts[1];
            Database db = DatabaseHandler.getDatabase();
            List<Place> places = db.getSelector().getAllPlacesForUnit(db.getSelector().getUnit(db.getSelector().getMunicipality(muni),name));

            if(db.getSelector().isChosen(db.getSelector().getUnit(db.getSelector().getMunicipality(muni),name).getId())){
                request.attribute("errDel", true);
                return serveAdminAddPlacePage.handle(request, response);
            }
            for(Place place : places){
                if(place.getStudent() != null){
                    request.attribute("errDel", true);
                    return serveAdminAddPlacePage.handle(request, response);
                }

            }

            db.getDeleter().deleteAllPlacesForUnit(db.getSelector().getUnit(db.getSelector().getMunicipality(muni),name));
            db.getDeleter().deleteUnitContent(db.getSelector().getUnit(db.getSelector().getMunicipality(muni),name));

            return serveAdminAddPlacePage.handle(request, response);

        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route handelAdminAddSingleUnitPost = (Request request, Response response) -> {
        if (isAdmin(request)) {
            Map<String, Object> model = new HashMap<>();

            String unitName = getQueryUnitName(request);
            String unitNumberOfSlots = getQueryUnitNumberOfSlots(request);
            String unitMunicipality = getQueryUnitMunicipality(request);
            String unitInfo = getQueryUnitInfo(request);
            Database db = DatabaseHandler.getDatabase();

            for(Unit unit : db.getSelector().getAllUnits()) {
                if (unit.getName().equals(unitName) && unit.getMunicipality().getName().equals(unitMunicipality)) {
                    request.attribute("errAdd", true);
                    return serveAdminAddPlacePage.handle(request, response);
                }
            }
            Unit unit = new Unit(db.getSelector().getMunicipality(unitMunicipality), unitName ,Integer.valueOf(unitNumberOfSlots),unitInfo);
            long unitId = db.getInserter().addUnit(unit);
            db.getInserter().addPlace(new Place(db.getSelector().getUnit(db.getSelector().getMunicipality(unitMunicipality),unitName)));
            return serveAdminAddPlacePage.handle(request, response);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route handleAdminAddHandledarePost = (Request request, Response response) -> {
        if (isAdmin(request)) {
            Map<String, Object> model = new HashMap<>();
            String handledareEmail = getQueryHandledareEmail(request);
            String handledareName = getQueryHandledareName(request);
            String handledarePhoneNumber = getQueryHandledarePhoneNumber(request);
            int id = Integer.valueOf(getQueryPlaceId(request));
            Database db = DatabaseHandler.getDatabase();
            model.put("handledare_email", handledareEmail);
            Handledare handledare;
            if(db.getSelector().handledareExistsOnPlace(handledareEmail)){
                return serveAdminAddPlacePage.handle(request, response);
            }

            String password = RandomGenerator.generateRandomPassword();
            MailSender sender = MailSenderProvider.getMailSender();
            if(handledareName == null && handledarePhoneNumber == null){
               handledare = new Handledare(handledareEmail, PasswordSecurity.createHash(password.toCharArray()));
               sender.sendMail(handledareEmail, "Lösenord till Vfusocionm", "Hej " + handledareName + "<br>" + "Ditt lösenord till "
               + "<a href='vfusocionom.hig.se'>" + "Vfusocionom.hig.se " + "</a>" + "<br>" +"Lösenord: " + password);
            }else if(handledarePhoneNumber == null){
                handledare = new Handledare(handledareEmail,handledareName,PasswordSecurity.createHash(password.toCharArray()));
                sender.sendMail(handledareEmail, "Lösenord till Vfusocionm", "Hej " + handledareName + "<br>" + "Ditt lösenord till "
                        + "<a href='vfusocionom.hig.se'>" + "Vfusocionom.hig.se" + "</a>" + "<br>" +"Lösenord: " + password);
            }
            else{
                handledare = new Handledare(handledareName,handledareEmail,handledarePhoneNumber,PasswordSecurity.createHash(password.toCharArray()));
                sender.sendMail(handledareEmail, "Lösenord till Vfusocionm", "Hej " + handledareName + "<br>" + "Ditt lösenord till "
                        + "<a href='vfusocionom.hig.se'>" + "Vfusocionom.hig.se " + "</a>" + "<br>" +"Lösenord: " + password);
            }

            db.getInserter().addHandledare(handledare);
            db.getInserter().addHandledareToPlace(db.getSelector().getPlace(id),db.getSelector().getHandledare(handledareEmail));
            return serveAdminAddPlacePage.handle(request, response);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };
    public static Route handleDeleteAllPost = (Request request, Response response) -> {
        if (isAdmin(request)) {
            String deleteOption = getQueryDeleteOption(request);
            Database db = DatabaseHandler.getDatabase();
            if(deleteOption == null){
                request.attribute("err", true);
                return serveAdminDeleteAllPage.handle(request, response);
            }
            if(deleteOption.equals("Studenter")){
                db.getDeleter().deleteAllStudentFromPlaceTable(db.getSelector().getAllStudents());
                db.getDeleter().deleteAllStudentContent();
                db.getDeleter().deleteAllStudentDataContent();
            }else if(deleteOption.equals("Handledare")){
                db.getDeleter().deleteAllHandledareFromPlaceTable(db.getSelector().getAllHandledare());
                db.getDeleter().deleteAllHandledareContent();
            }
            return serveAdminDeleteAllPage.handle(request, response);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route handleAdminDeleteHandledarePost = (Request request, Response response) -> {
        if (isAdmin(request)) {

            String handledareToDelete = getQueryHandledareToDelete(request);
            Database db = DatabaseHandler.getDatabase();
            db.getDeleter().deleteHandledareFromPlace(db.getSelector().getHandledare(handledareToDelete));
            db.getDeleter().deleteHandledareContent(db.getSelector().getHandledare(handledareToDelete));

            return serveAdminAddPlacePage.handle(request, response);

        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route handleUploadCsvResultPost = (Request request, Response response) -> {
        if (isAdmin(request)) {
            List<StudentData> studentDataList = request.session().attribute(ATTR_STUDENT_DATA_LIST);
            request.session().removeAttribute(ATTR_STUDENT_DATA_LIST);

            boolean userApprovedCsv = getQueryVerifyCsvButtonClicked(request).equals("okey");

            final String MODEL_USER_APPROVED_CSV = "csv_approved";

            Map<String, Object> model = new HashMap<>();
            model.put("page_title", "VFU-portal SOCIONOM");
            model.put("home_link", Path.Web.ADMIN_HOME);
            model.put(ATTR_ROLE, LoggedInRole.ADMIN.getRoleName());
            model.put(ATTR_NAME, request.session().attribute(ATTR_NAME));
            model.put(MODEL_USER_APPROVED_CSV, userApprovedCsv);

            if (userApprovedCsv) {
                final String MODEL_NUM_OF_CSV_STUDENTS = "csv_num_of_students";
                final String MODEL_NUM_OF_DB_STUDENTS = "db_num_of_students";
                final String MODEL_NUM_STUDENTS_ADDED = "num_of_students_added";
                int numOfStudentsInCsv = studentDataList.size();

                Database db = DatabaseHandler.getDatabase();
                List<String> allStudentDataEmails = db.getSelector().getAllStudentDataEmails();
                studentDataList.removeIf(studentData -> allStudentDataEmails.contains(studentData.getEmail()));
                db.getInserter().addStudentsViaCSV(studentDataList);
                MailSender mailSender = MailSenderProvider.getMailSender();
                for (StudentData studentData : studentDataList) {
                    Map<String, Object> emailModel = new HashMap<>();
                    emailModel.put("student_name", studentData.getName());
                    mailSender.sendMail(
                            studentData.getEmail(),
                            "VFU Socionom - Högskolan i Gävle",
                            ViewUtil.render(emailModel, Path.Template.EMAIL_STUDENT_ADDED)
                    );
                }

                model.put(MODEL_NUM_OF_CSV_STUDENTS, numOfStudentsInCsv);
                model.put(MODEL_NUM_OF_DB_STUDENTS, allStudentDataEmails.size());
                model.put(MODEL_NUM_STUDENTS_ADDED, studentDataList.size());
            }

            return render(model, Path.Template.ADMIN_ADD_STUDENTS_VERIFY_RESULT);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };
    public static Route handleAddSinglePlacePost = (Request request, Response response) -> {
        if (isAdmin(request)) {

            String unitName = getQueryUnitName(request);
            String unitMunicipality = getQueryUnitMunicipality(request);

            Database db = DatabaseHandler.getDatabase();
            Unit unit = db.getSelector().getUnit(db.getSelector().getMunicipality(unitMunicipality),unitName);
            db.getInserter().addOneNumberOfSlotsUnit(unit);
            db.getInserter().addSinglePlace(new Place(unit));
            return serveAdminAddPlacePage.handle(request,response);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };
    public static Route handleDeleteSinglePlacePost = (Request request, Response response) -> {
        if (isAdmin(request)) {

            String unitName = getQueryUnitName(request);
            String unitMunicipality = getQueryUnitMunicipality(request);

            Database db = DatabaseHandler.getDatabase();
            Unit unit = db.getSelector().getUnit(db.getSelector().getMunicipality(unitMunicipality),unitName);

            String placeID = getQueryPlaceId(request);
            Place place = db.getSelector().getPlace(Integer.valueOf(placeID));
            if(place.getStudent() != null){
                request.attribute("errDelPlaceStud", true);
                return serveAdminAddPlacePage.handle(request,response);
            }if(place.getHandledare() != null){
                db.getDeleter().deleteHandledareFromPlace(place.getHandledare());
                db.getDeleter().deleteHandledareContent(place.getHandledare());
            }
            db.getInserter().deleteOneNumberOfSlotsUnit(unit);
            db.getDeleter().deleteSinglePlace(db.getSelector().getPlace(Integer.valueOf(placeID)));
            return serveAdminAddPlacePage.handle(request,response);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route handleAddSingleStudentPost = (Request request, Response response) -> {
        if (isAdmin(request)) {
            Map<String, Object> model = new HashMap<>();
            model.put("page_title", "VFU-portal SOCIONOM");
            model.put("home_link", Path.Web.ADMIN_HOME);
            model.put(ATTR_ROLE, LoggedInRole.ADMIN.getRoleName());
            model.put(ATTR_NAME, request.session().attribute(ATTR_NAME));

            String studentName = getQueryStudentName(request);
            String studentEmail = getQueryStudentEmail(request);
            String studentYearBirth = getQueryStudentYearBirth(request);

            Database db = DatabaseHandler.getDatabase();
            boolean studentDataExists = db.getSelector().getStudentData(studentEmail) != null;
            model.put("student_data_exists", studentDataExists);

            if (!studentDataExists) {
                StudentData studentData = new StudentData(
                        studentEmail,
                        studentName,
                        studentYearBirth,
                        ""
                );
                model.put("student_name", studentName);
                model.put("student_email", studentEmail);
                model.put("student_year_birth", studentYearBirth);
                db.getInserter().addSingleStudent(studentData);
                Map<String, Object> emailModel = new HashMap<>();
                emailModel.put("student_name", studentName);
                MailSenderProvider.getMailSender().sendMail(
                        studentEmail,
                        "VFU Socionom - Högskolan i Gävle",
                        ViewUtil.render(emailModel, Path.Template.EMAIL_STUDENT_ADDED)
                );
            }

            return render(model, Path.Template.ADMIN_ADD_STUDENTS_SINGLE_VERIFY_RESULT);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route serveAdminHandleRegionsPage = (Request request, Response response) -> {
        if (isAdmin(request)) {
            Map<String, Object> model = new HashMap<>();
            model.put("page_title", "VFU-portal SOCIONOM");
            model.put("home_link", Path.Web.ADMIN_HOME);
            model.put(ATTR_ROLE, LoggedInRole.ADMIN.getRoleName());
            model.put(ATTR_NAME, request.session().attribute(ATTR_NAME));

            Database db = DatabaseHandler.getDatabase();

            if (isAddRegionNonAjax(request)) {
                putAddRegionResultToModel(model, request, db);
            } else if (isAddMuniNonAjax(request)) {
                putAddMuniResultToModel(model, request, db);
            } else if (isDeleteRegionNonAjax(request)) {
                putDeleteRegionResultToModel(model, request, db);
            } else if (isDeleteMuniNonAjax(request)) {
                putDeleteMuniResultToModel(model, request, db);
            }

            model.put("region_muni_list", getRegionMuniList(db));
            return render(model, Path.Template.ADMIN_HANDLE_REGIONS);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route serveAdminShowStudentsPage = (Request request, Response response) -> {
        if (isAdmin(request)) {
            Map<String, Object> model = new HashMap<>();
            model.put("page_title", "VFU-portal SOCIONOM");
            model.put("home_link", Path.Web.ADMIN_HOME);
            model.put(ATTR_ROLE, LoggedInRole.ADMIN.getRoleName());
            model.put(ATTR_NAME, request.session().attribute(ATTR_NAME));

            List<Map.Entry<StudentData, Student>> allStudentsInfo = new ArrayList<>();
            DatabaseSelector dbSelector = DatabaseHandler.getDatabase().getSelector();
            List<StudentData> allStudentData = dbSelector.getAllStudentData();
            List<Student> allStudents = dbSelector.getAllStudents();
            for (StudentData studentData : allStudentData) {
                List<Student> studentSearch = allStudents.stream()
                        .filter(s -> s.getEmail().equals(studentData.getEmail()))
                        .collect(Collectors.toList());
                Student student = null;
                if (studentSearch.size() > 0) {
                    student = studentSearch.get(0);
                }
                allStudentsInfo.add(new AbstractMap.SimpleEntry<>(studentData, student));
            }

            model.put("studentsInfo", allStudentsInfo);
            model.put("total_num_students", allStudentData.size());
            model.put("num_stud_region", allStudents.size());
            int numChoices = 0;
            for (Student student : allStudents) {
                if (student.getChoice_1().getId() != 0) {
                    numChoices++;
                }
            }
            model.put("num_stud_choices", numChoices);

            return render(model, Path.Template.ADMIN_SHOW_STUDENTS);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route handleDeleteStudentPost = (Request request, Response response) -> {
        if (isAdmin(request)) {
            String email = getQueryStudentToDelete(request);
            DatabaseDeleter dbDeleter = DatabaseHandler.getDatabase().getDeleter();
            dbDeleter.deleteStudentFromPlace(email);
            dbDeleter.deleteStudentContent(email);
            dbDeleter.deleteStudentDataContent(email);
            response.redirect(Path.Web.ADMIN_SHOW_STUDENTS);
            return null;
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route serveAdminShowVfuSamPage = (Request request, Response response) -> {
        if (isAdmin(request)) {
            Map<String, Object> model = new HashMap<>();
            model.put("page_title", "VFU-portal SOCIONOM");
            model.put("home_link", Path.Web.ADMIN_HOME);
            model.put(ATTR_ROLE, LoggedInRole.ADMIN.getRoleName());
            model.put(ATTR_NAME, request.session().attribute(ATTR_NAME));
            Database db = DatabaseHandler.getDatabase();
            List<VFUSamordnare> vfuSamordnare = db.getSelector().getAllVFUSamordnare();
            int vfuSamordnareNoPass = 0;
            for (VFUSamordnare vfuSam : vfuSamordnare) {
                String hashedPw = vfuSam.getHashedPassword();
                if (hashedPw == null || hashedPw.equals("")) {
                    vfuSamordnareNoPass++;
                }
            }
            model.put("total_num_vfu_sam", vfuSamordnare.size());
            model.put("total_num_vfu_sam_not_pass", vfuSamordnareNoPass);
            model.put("vfu_samordnare", vfuSamordnare);
            model.put("vfu_samordnare_reg_links", db.getSelector().getAllVfuRegLinks());
            return render(model, Path.Template.ADMIN_SHOW_VFU_SAM);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };


    public static Route handleDeleteVfuSamPost = (Request request, Response response) -> {
        if (isAdmin(request)) {
            String email = getQueryVfuSamToDelete(request);
            DatabaseHandler.getDatabase().getDeleter().deleteVFUSamContent(email);
            response.redirect(Path.Web.ADMIN_SHOW_VFU_SAM);
            return null;
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };
    public static Route handleUpdatePhoneNumberPost = (Request request, Response response) -> {
        if (isAdmin(request)) {
            String email = getQueryHandledareToUpdate(request);
            String phoneNumber = getQueryPhonenumberToUpdate(request);
            DatabaseHandler.getDatabase().getInserter().updatePhonenumberHandledare(phoneNumber,email);
            response.redirect(Path.Web.ADMIN_ADD_PLACE);
            return null;
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route handleDeleteStudentFromPlacePost = (Request request, Response response) -> {
        if (isAdmin(request)) {
            String email = getQueryStudentToDeleteFromPlace(request);
            DatabaseHandler.getDatabase().getDeleter().deleteStudentFromPlace(email);
            response.redirect(Path.Web.ADMIN_MATCH);
            return null;
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route serveAdminMatchPage = (Request request, Response response) -> {
        if (isAdmin(request)) {
            Map<String, Object> model = new HashMap<>();
            model.put("page_title", "VFU-portal SOCIONOM");
            model.put("home_link", Path.Web.ADMIN_HOME);
            model.put(ATTR_ROLE, LoggedInRole.ADMIN.getRoleName());
            model.put(ATTR_NAME, request.session().attribute(ATTR_NAME));

            DatabaseSelector dbSelector = DatabaseHandler.getDatabase().getSelector();
            List<Place> allPlaces = dbSelector.getAllPlaces();
            List<StudentData> allStudentData = dbSelector.getAllStudentData();
            List<Student> allStudents = dbSelector.getAllStudents();
            List<String> allRegions = dbSelector.getAllRegionNames();
            List<Place> allPlacesWithoutStudent = dbSelector.getAllPlacesWithoutStudent();
            List<Place> allPlacesWithStudent = dbSelector.getAllPlacesWithStudent();
            Map region_students = new HashMap();
            Map region_places = new HashMap();
            for(String region : allRegions) {
                int students = 0;
                int places = 0;
                for(Student student : allStudents) {
                    if(student.getRegion().getName().equals(region)){
                        students++;
                    }
                }
                for(Place place : allPlaces) {
                    if(place.getUnit().getMunicipality().getRegion().getName().equals(region)){
                        places++;
                    }
                }
                region_places.put(region, places);
                region_students.put(region, students);

            }
            model.put("region_students", region_students);
            model.put("region_places", region_places);
            model.put("all_places", allPlaces);
            model.put("all_regions", allRegions);
            model.put("total_num_students", allStudentData.size());
            model.put("num_stud_region", allStudents.size());
            model.put("all_students_without_place",  allStudentData.size() - allPlacesWithStudent.size());
            model.put("all_places_without_student", allPlacesWithoutStudent.size());

            List<Student> unmatchedStudents = new ArrayList<>();
            for (Student student : allStudents) {
                boolean studentIsUnmatched = true;
                for (Place place : allPlaces) {
                    if (place.getStudent() != null && place.getStudent().getEmail().equals(student.getEmail())) {
                        studentIsUnmatched = false;
                        break;
                    }
                }
                if (studentIsUnmatched) {
                    unmatchedStudents.add(student);
                }
            }
            model.put("unmatched_students", unmatchedStudents);
            boolean emailSent = request.session().attribute("mail_sent") != null;
            if (emailSent) {
                model.put("mail_sent", true);
                request.session().removeAttribute("mail_sent");
            }
            return render(model, Path.Template.ADMIN_MATCH);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route handleDoMatchPost = (Request request, Response response) -> {
        if (isAdmin(request)) {
            Map<String, Object> model = new HashMap<>();
            model.put("page_title", "VFU-portal SOCIONOM");
            model.put("home_link", Path.Web.ADMIN_HOME);
            model.put(ATTR_ROLE, LoggedInRole.ADMIN.getRoleName());
            model.put(ATTR_NAME, request.session().attribute(ATTR_NAME));

            Match match = new Match();
            DatabaseSelector dbSelector = DatabaseHandler.getDatabase().getSelector();
            List<Place> allPlaces = dbSelector.getAllPlaces();
            List<Student> allStudents = dbSelector.getAllStudents();

            List<MatchStudentPlace> matchResult = match.getMatchEachPlace(allPlaces, allStudents);
            request.session().attribute("match_result", matchResult);
            model.put("match_result", matchResult);

            return render(model, Path.Template.ADMIN_MATCH_VERIFY);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route handleDoMatchVerifyPost = (Request request, Response response) -> {
        if (isAdmin(request)) {
            boolean adminApprovedMatch = getQueryVerifyCsvButtonClicked(request).equals("okey");
            if (adminApprovedMatch) {
                List<MatchStudentPlace> matchResult = request.session().attribute("match_result");
                DatabaseInserter dbInserter = DatabaseHandler.getDatabase().getInserter();
                for (MatchStudentPlace match : matchResult) {
                    Place place = match.getPlace();
                    Student student = match.getStudent();
                    dbInserter.addStudentToPlace(place, student);
                }
            }
            request.session().removeAttribute("match_result");
            response.redirect(Path.Web.ADMIN_MATCH);
            return null;
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route handleManualMatchPost = (Request request, Response response) -> {
        if (isAdmin(request)) {
            int placeId = Integer.valueOf(getQueryPlaceId(request));
            String email = getQueryEmail(request);
            Database db = DatabaseHandler.getDatabase();
            db.getInserter().addStudentToPlace(placeId, email);
            response.redirect(Path.Web.ADMIN_MATCH);
            return null;
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route handleSendEmailMatchedStudentsPost = (Request request, Response response) -> {
        if (isAdmin(request)) {
            List<Place> allPlacesWithStudent = DatabaseHandler.getDatabase().getSelector().getAllPlacesWithStudent();
            MailSender mailSender = MailSenderProvider.getMailSender();
            for (Place place : allPlacesWithStudent) {
                Student student = place.getStudent();
                Map<String, Object> emailModel = new HashMap<>();
                emailModel.put("student_name", student.getStudentData().getName());
                emailModel.put("unit_name",
                        place.getUnit().getName() + " (" + place.getUnit().getMunicipality().getRegion().getName() + ")"
                );
                Handledare handledare = place.getHandledare();
                boolean handledareExists = handledare != null;
                emailModel.put("handledare_exists", handledareExists);
                if (handledareExists) {
                    emailModel.put("handledare", handledare.getName() + " (" + handledare.getEmail() + ")");
                }
                mailSender.sendMail(
                        student.getEmail(),
                        "VFU Socionom - Högskolan i Gävle",
                        ViewUtil.render(emailModel, Path.Template.EMAIL_STUDENT_MATCHED)
                );
                request.session().attribute("mail_sent", true);
            }
            response.redirect(Path.Web.ADMIN_MATCH);
            return null;
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route serveAdminAddVfuSamPage = (Request request, Response response) -> {
        if (isAdmin(request)) {
            Map<String, Object> model = new HashMap<>();
            model.put("page_title", "VFU-portal SOCIONOM");
            model.put("home_link", Path.Web.ADMIN_HOME);
            model.put(ATTR_ROLE, LoggedInRole.ADMIN.getRoleName());
            model.put(ATTR_NAME, request.session().attribute(ATTR_NAME));
            Database db = DatabaseHandler.getDatabase();
            model.put("all_regions", db.getSelector().getAllRegionNames());
            String errorMsg = request.attribute("error_msg");
            if (errorMsg != null) {
                model.put("error", true);
                model.put("error_msg", errorMsg);
            } else if (request.attribute("vfu_sam_added") != null) {
                model.put("vfu_sam_added", true);
                model.put("vfu_sam_name", request.attribute("vfu_sam_name"));
                model.put("vfu_sam_email", request.attribute("vfu_sam_email"));
            }
            return render(model, Path.Template.ADMIN_ADD_VFU_SAM);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route handleAddVfuSamPost = (Request request, Response response) -> {
        if (isAdmin(request)) {
            String name = getQueryVfuSamName(request);
            String email = getQueryEmail(request);
            String tfn = getQueryVfuTfn(request);
            String region = getQueryRegion(request);
            if (name == null || name.length() < 2) {
                request.attribute("error_msg", "Var god ange namn");
                return serveAdminAddVfuSamPage.handle(request, response);
            }
            Database db = DatabaseHandler.getDatabase();
            boolean vfuSamAlreadyExists = db.getSelector().getVFUSamordnare(email) != null;
            if (vfuSamAlreadyExists) {
                request.attribute("error_msg", "VFU Samordnaren med email '" + email + "' fanns redan");
                return serveAdminAddVfuSamPage.handle(request, response);
            }
            if (region == null) {
                request.attribute("error_msg", "Var god ange studieort");
                return serveAdminAddVfuSamPage.handle(request, response);
            }
            if (!db.getSelector().regionExists(region)) {
                request.attribute("error_msg", "Ogiltig studieort");
                return serveAdminAddVfuSamPage.handle(request, response);
            }
            request.attribute("vfu_sam_added", true);
            request.attribute("vfu_sam_name", name);
            request.attribute("vfu_sam_email", email);
            db.getInserter().addVFUSamordnare(new VFUSamordnare(
                    name, email, tfn, new Region(region), ""
            ));
            String regLink = RandomGenerator.generateRegLink();
            String regLinkFull = request.scheme() + "://" + request.host() + "/reg/vfu-samordnare/" + regLink + "/";
            db.getInserter().addLinkMailVFU(new LinkMailVFUSamordnare(email, regLink));
            Map<String, Object> emailModel = new HashMap<>();
            emailModel.put("vfu_name", name);
            emailModel.put("reg_link", regLinkFull);
            MailSenderProvider.getMailSender().sendMail(
                    email,
                    "VFU Samordnare registrering",
                    ViewUtil.render(emailModel, Path.Template.EMAIL_REG_VFU_SAMORDNARE)
            );
            return serveAdminAddVfuSamPage.handle(request, response);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    private static boolean isAddRegionNonAjax(Request request) {
        Object attrAddRegion = request.attribute(ATTR_ADD_REGION);
        return attrAddRegion != null && (boolean) attrAddRegion;
    }

    private static void putAddRegionResultToModel(Map<String, Object> model,
                                                  Request request,
                                                  Database db) throws DatabaseException {
        final String MODEL_REGION = "region";
        String region = getQueryRegion(request);
        model.put(MODEL_REGION, region);
        if (db.getSelector().regionExists(region)) {
            final String MODEL_REGION_EXISTED = "region_existed";
            model.put(MODEL_REGION_EXISTED, true);
        } else {
            final String MODEL_REGION_ADDED = "region_added";
            model.put(MODEL_REGION_ADDED, true);
            db.getInserter().addRegion(new Region(region));
        }
    }

    private static boolean isAddMuniNonAjax(Request request) {
        Object attrAddMuni = request.attribute(ATTR_ADD_MUNI);
        return attrAddMuni != null && (boolean) attrAddMuni;
    }

    private static void putAddMuniResultToModel(Map<String, Object> model,
                                                  Request request,
                                                  Database db) throws DatabaseException {
        final String MODEL_MUNI = "muni";
        String muni = getQueryMuni(request);
        model.put(MODEL_MUNI, muni);
        if (db.getSelector().municipalityExists(muni)) {
            model.put("muni_existed", true);
        } else {
            model.put("muni_added", true);
            db.getInserter().addMunicipality(new Municipality(muni, new Region(request.params("regionName"))));
        }
    }

    private static boolean isDeleteRegionNonAjax(Request request) {
        Object attrDeleteRegion = request.attribute(ATTR_DELETE_REGION);
        return attrDeleteRegion != null && (boolean) attrDeleteRegion;
    }

    private static void putDeleteRegionResultToModel(Map<String, Object> model,
                                                Request request,
                                                Database db) throws DatabaseException {
        final String MODEL_REGION = "region";
        String regionToDelete = getQueryRegionToDelete(request);
        model.put(MODEL_REGION, regionToDelete);
        try {
            db.getDeleter().deleteRegionAndMuniContent(new Region(regionToDelete));
            model.put("region_deleted", true);
        } catch (DatabaseException e) {
            model.put("region_foreign_key", true);
        }
    }

    private static boolean isDeleteMuniNonAjax(Request request) {
        Object attrDeleteMuni = request.attribute(ATTR_DELETE_MUNI);
        return attrDeleteMuni != null && (boolean) attrDeleteMuni;
    }

    private static void putDeleteMuniResultToModel(Map<String, Object> model,
                                                Request request,
                                                Database db) throws DatabaseException {
        final String MODEL_MUNI = "muni";
        String muniToDelete = getQueryMuniToDelete(request);
        model.put(MODEL_MUNI, muniToDelete);
        try {
            db.getDeleter().deleteMuniContent(muniToDelete);
            model.put("muni_deleted", true);
        } catch (DatabaseException e) {
            model.put("muni_foreign_key", true);
        }
    }

    private static List<Map.Entry<String, List<String>>> getRegionMuniList(Database db) throws DatabaseException {
        List<Map.Entry<String, List<String>>> regionMuniList = new ArrayList<>();
        DatabaseSelector dbSelector = db.getSelector();
        List<String> allRegionNames = dbSelector.getAllRegionNames();
        for (String regionName : allRegionNames) {
            regionMuniList.add(new AbstractMap.SimpleEntry<>(
                    regionName,
                    dbSelector.getAllMunicipalitiesForRegion(regionName))
            );
        }
        return regionMuniList;
    }

    public static Route handleAddRegionPost = (Request request, Response response) -> {
        if (isAdmin(request)) {
            request.attribute(ATTR_ADD_REGION, true);
            return serveAdminHandleRegionsPage.handle(request, response);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route handleAddRegionPostAjax = (Request request, Response response) -> {
        if (isAdmin(request)) {
            String region = request.body();
            Database db = DatabaseHandler.getDatabase();
            JsonHelper jsonHelper = JsonHandler.getJsonHelper();
            if (db.getSelector().regionExists(region)) {
                return jsonHelper.keyBooleanToJson("alreadyExisted", true);
            } else {
                db.getInserter().addRegion(new Region(region));
                List<String> regionMunis = db.getSelector().getAllMunicipalitiesForRegion(region);
                return jsonHelper.keyBooleanKeyListToJson(
                        "alreadyExisted", false,
                        "regionMunis", regionMunis
                );
            }
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route handleAddMuniPost = (Request request, Response response) -> {
        if (isAdmin(request)) {
            request.attribute(ATTR_ADD_MUNI, true);
            return serveAdminHandleRegionsPage.handle(request, response);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route handleAddMuniPostAjax = (Request request, Response response) -> {
        if (isAdmin(request)) {
            String muni = request.body();
            Database db = DatabaseHandler.getDatabase();
            JsonHelper jsonHelper = JsonHandler.getJsonHelper();
            if (db.getSelector().municipalityExists(muni)) {
                return jsonHelper.keyBooleanToJson("alreadyExisted", true);
            } else {
                db.getInserter().addMunicipality(new Municipality(muni, new Region(request.params("regionName"))));
                return jsonHelper.keyBooleanToJson("alreadyExisted", false);
            }
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route handleDeleteRegionPost = (Request request, Response response) -> {
        if (isAdmin(request)) {
            request.attribute(ATTR_DELETE_REGION, true);
            return serveAdminHandleRegionsPage.handle(request, response);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route handleDeleteRegionPostAjax = (Request request, Response response) -> {
        if (isAdmin(request)) {
            String regionToDelete = request.body();
            Database db = DatabaseHandler.getDatabase();
            try {
                db.getDeleter().deleteRegionAndMuniContent(new Region(regionToDelete));
            } catch (DatabaseException e) {
                response.status(409);
            }
            return "";
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route handleDeleteMuniPost = (Request request, Response response) -> {
        if (isAdmin(request)) {
            request.attribute(ATTR_DELETE_MUNI, true);
            return serveAdminHandleRegionsPage.handle(request, response);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route handleDeleteMuniPostAjax = (Request request, Response response) -> {
        if (isAdmin(request)) {
            String muniToDelete = request.body();
            Database db = DatabaseHandler.getDatabase();
            try {
                db.getDeleter().deleteMuniContent(muniToDelete);
            } catch (DatabaseException e) {
                response.status(409);
            }
            return "";
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    private static String getQueryVerifyCsvButtonClicked(Request request) {
        return request.queryParams("button_clicked");
    }

    private static String getQueryStudentName(Request request) {
        return request.queryParams("studentLName") + ", " + request.queryParams("studentFName");
    }

    private static String getQueryStudentEmail(Request request) {
        return request.queryParams("studentId"); //+ "@student.hig.se";
    }

    private static String getQueryEmail(Request request) {
        return request.queryParams("email");
    }

    private static String getQueryVfuTfn(Request request) {
        return request.queryParams("vfuTfn");
    }

    private static String getQueryVfuSamName(Request request) {
        return request.queryParams("vfuName");
    }

    private static String getQueryStudentYearBirth(Request request) {
        return request.queryParams("studentYearBirth");
    }

    private static String getQueryRegion(Request request) {
        return request.queryParams("region");
    }

    private static String getQueryMuni(Request request) {
        return request.queryParams("muni");
    }

    private static String getQueryRegionToDelete(Request request) {
        return request.queryParams("regionToDelete");
    }

    private static String getQueryMuniToDelete(Request request) {
        return request.queryParams("muniToDelete");
    }
    private static String getQueryUnitMunicipality(Request request) {
        return request.queryParams("regionMunis");
    }

    private static String getQueryUnitInfo(Request request) {
        return request.queryParams("unitInfo");
    }
    private static String getQueryUnitNumberOfSlots(Request request) {
        return request.queryParams("unitNumberOfSlots");
    }
    private static String getQueryUnitName(Request request) {
        return request.queryParams("unitName");
    }

    private static String getQueryHandledareToDelete(Request request) {
        return request.queryParams("handledareToDelete");
    }
    private static String getQueryHandledareEmail(Request request) {
        return request.queryParams("handledareToAdd");
    }
    private static String getQueryHandledareName(Request request) {
        return request.queryParams("handledareName");
    }
    private static String getQueryHandledarePhoneNumber(Request request) {
        return request.queryParams("handledarePhoneNumber");
    }
    private static String getQueryUnitToDelete(Request request) {
        return request.queryParams("unitToDelete");
    }
    private static String getQueryPlaceId(Request request) {
        return request.queryParams("placeId");
    }
    private static String getQueryVfuSamToDelete(Request request) {
        return request.queryParams("vfuSamToDel");
    }
    private static String getQueryStudentToDelete(Request request) {
        return request.queryParams("studentToDel");
    }
    private static String getQueryStudentToDeleteFromPlace(Request request) {
        return request.queryParams("studentToDelFromPlace");
    }
    private static String getQueryHandledareToUpdate(Request request) {
        return request.queryParams("handledareToUpdate");
    }
    private static String getQueryPhonenumberToUpdate(Request request) {
        return request.queryParams("phonenumberToUpdate");
    }

    private static String getQueryDeleteOption(Request request) {
        return request.queryParams("deleteOption");
    }

}
