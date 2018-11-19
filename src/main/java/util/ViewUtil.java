package util;

import roles.LoggedInRole;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static login.LoginController.*;
import static login.LoginController.ATTR_ROLE;

/**
 * Hjälp klass för thymeleaf/template rendrering, samt sköter vilken sida som visas vid 404 och 500
 */
public class ViewUtil {

    public static String render(Map<String, Object> model, String templatePath) {
        return new ThymeleafTemplateEngine().render(new ModelAndView(model, templatePath));
    }

    public static Route notFound = (Request request, Response response) ->
        displayErrorPage(request, "404 Not Found", "Kunde tyvärr ej hitta sidan du söker");

    public static Route internalServerError = (Request request, Response response) ->
        displayErrorPage(request, "500 Internal Server Error", "Något gick fel");

    private static String displayErrorPage(Request request, String title, String message) {
        Map<String, Object> model = new HashMap<>();
        model.put("page_title", "VFU-portal SOCIONOM");
        model.put("text_title", title);
        model.put("text_content", message);
        if (isLoggedIn(request)) {
            model.put(ATTR_NAME, request.session().attribute(ATTR_NAME));
            if (isAdmin(request)) {
                model.put(ATTR_ROLE, LoggedInRole.ADMIN.getRoleName());
                model.put("home_link", Path.Web.ADMIN_HOME);
                return render(model, Path.Template.ADMIN_ERROR);
            } else if (isStudent(request)) {
                model.put(ATTR_ROLE, LoggedInRole.STUDENT.getRoleName());
                model.put("home_link", Path.Web.STUDENT_HOME);
                return render(model, Path.Template.STUDENT_ERROR);
            } else if (isHandledare(request)) {
                model.put(ATTR_ROLE, LoggedInRole.HANDLEDARE.getRoleName());
                model.put("home_link", Path.Web.HANDLEDARE_HOME);
                return render(model, Path.Template.HANDLEDARE_ERROR);
            } else if (isVfuSamordnare(request)) {
                model.put(ATTR_ROLE, LoggedInRole.VFU_SAMORDNARE.getRoleName());
                model.put("home_link", Path.Web.VFU_SAMORDNARE_HOME);
                return render(model, Path.Template.VFU_SAMORDNARE_ERROR);
            }
        }

        return render(model, Path.Template.ERROR);
    }

}
