package student;

import data.StudentData;
import database.Database;
import database.DatabaseException;
import database.DatabaseHandler;
import organisations.Region;
import organisations.Unit;
import roles.LoggedInRole;
import roles.Student;
import spark.Request;
import spark.Response;
import spark.Route;
import util.Path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static login.LoginController.*;
import static util.ViewUtil.render;

/**
 * Hanterar alla request för studenter på sidan
 */
public class StudentController {

    public static Route serveStudentHomePage = (Request request, Response response) -> {
        if (isStudent(request)) {
            Map<String, Object> model = getInitialStudentModel();
            Database db = DatabaseHandler.getDatabase();
            String email = getStudentEmail(request);
            Student student = db.getSelector().getStudent(email);
            if (studentPerformedFirstPage(student)) {
                if (studentMadeChoices(student)) {
                    return serveStudentStatus(student, model);
                } else {
                    return serveStudentChoice(db, student, model, request);
                }
            } else {
                return serveStudentFirst(db, model, email);
            }
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    private static String serveStudentFirst(Database db, Map<String, Object> model, String email)
            throws DatabaseException {
        StudentData studentData = db.getSelector().getStudentData(email);
        model.put(ATTR_NAME, studentData.getName());
        putStudentDataFirstPageModel(model, studentData, db);
        model.put("html_content", db.getSelector().getStudentFirstPageHtml());
        return render(model, Path.Template.STUDENT_FIRST);
    }

    private static String serveStudentChoice(Database db, Student student, Map<String, Object> model, Request request)
            throws DatabaseException {
        StudentData studentData = student.getStudentData();
        model.put(ATTR_NAME, studentData.getName());
        List<Unit> allUnitsForRegion = db.getSelector()
                .getAllUnitFromRegionForStudentChoice(student.getRegion().getName());
        List<Unit> allUnitsWithPlacesForRegion = new ArrayList<>();

        for (Unit unit: allUnitsForRegion) {
            if(db.getSelector().getnumberOfUnreservedPlacesInUnit(unit.getId()) > 0){
                allUnitsWithPlacesForRegion.add(unit);
            }
        }

        request.session().attribute("allUnitsForRegion", allUnitsWithPlacesForRegion);
        model.put("unitsForRegion", allUnitsWithPlacesForRegion);
        model.put("region", student.getRegion().getName());
        String errorMsg = request.attribute("error_msg");
        if (errorMsg != null) {
            model.put("error", true);
            model.put("error_msg", errorMsg);
        }
        return render(model, Path.Template.STUDENT_CHOICE);
    }

    private static String serveStudentStatus(Student student, Map<String, Object> model) throws DatabaseException{
        StudentData studentData = student.getStudentData();
        model.put(ATTR_NAME, studentData.getName());
        model.put("match_info", DatabaseHandler.getDatabase().getSelector().getPlaceViaStudent(studentData.getEmail()));
        model.put("stud_email", studentData.getEmail());
        model.put("stud_yob", studentData.getDob());
        model.put("stud_tfn", studentData.getPhoneNumber());
        model.put("stud_region", student.getRegion().getName());
        model.put("stud_pbrev", student.getPersonalLetter());
        model.put("stud_choice1", student.getChoice_1().getName());
        model.put("stud_choice2", student.getChoice_2().getName());
        model.put("stud_choice3", student.getChoice_3().getName());
        model.put("stud_choice4", student.getChoice_4().getName());
        model.put("stud_choice5", student.getChoice_5().getName());
        model.put("html_content", DatabaseHandler.getDatabase().getSelector().getStudentStatusPageHtml());
        return render(model, Path.Template.STUDENT_STATUS);
    }

    private static boolean studentPerformedFirstPage(Student student) {
        return student != null;
    }

    private static boolean studentMadeChoices(Student student) {
        return student.getChoice_1().getId() != 0;
    }

    public static Route handleApplyFirst = (Request request, Response response) -> {
        if (isStudent(request)) {
            Database db = DatabaseHandler.getDatabase();
            String studentEmail = getStudentEmail(request);
            if (studentPerformedFirstPage(db.getSelector().getStudent(studentEmail))) {
                response.redirect(Path.Web.STUDENT_HOME);
                return null;
            }

            Map<String, Object> model = getInitialStudentModel();
            StudentData studentData = db.getSelector().getStudentData(studentEmail);
            model.put(ATTR_NAME, studentData.getName());
            putStudentDataFirstPageModel(model, studentData, db);

            String region;
            String phoneNumber;
            String personalLetter;
            try {
                region = getQueryRegion(request);
                phoneNumber = getQueryTfnNumber(request);
                personalLetter = getQueryPersonalLetter(request);
            } catch (Exception e) {
                model.put("error", true);
                // max 200 000 bytes med default jetty ≈ 200 000 tecken
                // personligt brev fältet tillåter max 10 000 tecken men användaren skulle själv kunna ändra
                // det i html koden, därför behövs denna kontroll
                model.put("error_msg", "För stort form data");
                model.put("html_content", db.getSelector().getStudentFirstPageHtml());
                return render(model, Path.Template.STUDENT_FIRST);
            }

            if (region == null) {
                model.put("error", true);
                model.put("error_msg", "Måste välja studieort");
                model.put("html_content", db.getSelector().getStudentFirstPageHtml());
                return render(model, Path.Template.STUDENT_FIRST);
            }

            if (personalLetter == null || personalLetter.length() < 2) {
                model.put("error", true);
                model.put("error_msg", "Måste skriva personligt brev");
                model.put("html_content", db.getSelector().getStudentFirstPageHtml());
                return render(model, Path.Template.STUDENT_FIRST);
            }

            if (personalLetter.length() > 10_000) {
                model.put("error", true);
                model.put("error_msg", "Personligt brev max längd 10 000 tecken");
                model.put("html_content", db.getSelector().getStudentFirstPageHtml());
                return render(model, Path.Template.STUDENT_FIRST);
            }

            if (!studentData.getPhoneNumber().equals(phoneNumber)) {
                db.getInserter().updatePhoneNumberStudent(phoneNumber, studentEmail);
            }

            db.getInserter().addStudentFirstPage(studentEmail, new Region(region), personalLetter);

            response.redirect(Path.Web.STUDENT_HOME);
            return null;
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route handleConfirmLetter = (Request request, Response response) -> {
        if (isStudent(request)) {
            response.redirect(Path.Web.STUDENT_HOME);
        }

        return null;
        };

    public static Route handleApplyChoice = (Request request, Response response) -> {
        if (isStudent(request)) {
            int[] choices;
            try {
                choices = validateAndGetQueryUnitCoices(request);
            } catch (Exception e) {
                request.attribute("error_msg", e.getMessage());
                return serveStudentHomePage.handle(request, response);
            }
            DatabaseHandler.getDatabase().getInserter().setStudentChoices(getStudentEmail(request), choices);
            request.session().removeAttribute("allUnitsForRegion");
            response.redirect(Path.Web.STUDENT_HOME);
            return null;
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    private static String getStudentEmail(Request request) {
        return request.session().attribute(ATTR_NAME);// + "@student.hig.se";
    }

    private static Map<String, Object> getInitialStudentModel() {
        Map<String, Object> model = new HashMap<>();
        model.put("page_title", "VFU-portal SOCIONOM");
        model.put("home_link", Path.Web.STUDENT_HOME);
        model.put(ATTR_ROLE, LoggedInRole.STUDENT.getRoleName());
        return model;
    }

    private static void putStudentDataFirstPageModel(Map<String, Object> model,
                                                     StudentData studentData,
                                                     Database db) throws DatabaseException {
        model.put("stud_email", studentData.getEmail());
        model.put("stud_yob", studentData.getDob());
        model.put("stud_tfn", studentData.getPhoneNumber());
        model.put("all_regions", db.getSelector().getAllRegionNames());
    }

    private static String getQueryRegion(Request request) {
        return request.queryParams("studieort");
    }

    private static String getQueryTfnNumber(Request request) {
        return request.queryParams("tfnnummer");
    }

    private static String getQueryPersonalLetter(Request request) {
        return request.queryParams("pbrev");
    }

    private static int[] validateAndGetQueryUnitCoices(Request request) throws Exception {
        List<Unit> allUnitsForRegion = request.session().attribute("allUnitsForRegion");
        int numOfUnits = allUnitsForRegion.size();
        int numOfChoices = numOfUnits >= 5 ? 5 : numOfUnits;
        List<Integer> choices = new ArrayList<>();
        for (int i = 0; i < numOfChoices; i++) {
            String choiceString = request.queryParams("choice" + (i+1));
            if (choiceString == null) {
                throw new Exception("Ogiltiga val, du måste välja något");
            }
            int choice;
            try {
                choice = Integer.parseInt(choiceString);
            } catch (NumberFormatException e) {
                throw new Exception("Ogiltiga val, mixtra inte med HTML koden");
            }
            if (choices.contains(choice)) {
                throw new Exception("Ogiltiga val, du kan inte välja samma på flera val");
            }
            choices.add(choice);
        }

        choices.forEach(choice -> {
            boolean choicePointsToExistingUnitId = allUnitsForRegion.stream().anyMatch(unit -> choice == unit.getId());
            if (!choicePointsToExistingUnitId) {
                throw new RuntimeException("Ogiltiga val, mixtra inte med HTML koden");
            }
        });

        return choices.stream().mapToInt(i -> i).toArray();
    }

}
