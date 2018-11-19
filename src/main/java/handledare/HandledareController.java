package handledare;

import database.DatabaseHandler;
import organisations.Place;
import roles.LoggedInRole;
import spark.Request;
import spark.Response;
import spark.Route;
import util.Path;

import java.util.HashMap;
import java.util.Map;

import static login.LoginController.*;
import static util.ViewUtil.render;
/**
 * Hanterar alla requests för handledare på sidan
 */
public class HandledareController {

    public static Route serveHandledareHomePage = (Request request, Response response) -> {
        if (isHandledare(request)) {
            Map<String, Object> model = new HashMap<>();
            model.put("page_title", "VFU-portal SOCIONOM");
            model.put("text_title", "Välkommen handledare");
            Place place = DatabaseHandler.getDatabase().getSelector().getPlaceViaHandledare(request.session().attribute(ATTR_NAME));

            if (place.getStudent() == null){
                model.put("contact_student_name", "Ingen tilldelad student");
                model.put("contact_student_email", "Ingen tilldelad student");
                model.put("contact_student_phone_number", "Ingen tilldelad student");
            }else{
                model.put("contact_student_name", place.getStudent().getStudentData().getName());
                model.put("contact_student_email", place.getStudent().getStudentData().getEmail());
                model.put("contact_student_phone_number", place.getStudent().getStudentData().getPhoneNumber());
            }

            model.put("home_link", Path.Web.HANDLEDARE_HOME);
            model.put(ATTR_ROLE, LoggedInRole.HANDLEDARE.getRoleName());
            model.put(ATTR_NAME, request.session().attribute(ATTR_NAME));
            return render(model, Path.Template.HANDLEDARE_HOME);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route serveHandledareCriteriaPage = (Request request, Response response) -> {
        if (isHandledare(request)) {
            Map<String, Object> model = new HashMap<>();
            model.put("home_link", Path.Web.HANDLEDARE_HOME);
            model.put(ATTR_ROLE, LoggedInRole.HANDLEDARE.getRoleName());
            model.put(ATTR_NAME, request.session().attribute(ATTR_NAME));
            return render(model, Path.Template.HANDLEDAER_CRITERIA);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }

    };

}
