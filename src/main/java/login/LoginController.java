package login;

import roles.LoggedInRole;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import util.Path;

import java.util.HashMap;
import java.util.Map;

import static auth.Authenticator.authenticateAdmin;
import static auth.Authenticator.authenticateStudent;
import static auth.Authenticator.authenticateHandledare;
import static auth.Authenticator.authenticateVFUSamordnare;

import static util.RedirectUtil.redirectToRoleHome;
import static util.ViewUtil.render;

/**
 * Hanterar alla request för logins
 */
public class LoginController {

    public static String ATTR_ROLE = "role";
    public static String ATTR_NAME = "name";

    public static Route serveLoginPage = (Request request, Response response) -> {
        if (isLoggedIn(request)) {
            redirectToRoleHome(request, response);
            return null;
        }

        Map<String, Object> model = new HashMap<>();
        model.put("page_title", "VFU-portal SOCIONOM");
        model.put("text_title", "Logga in");
        model.put("email_input", "");
        return render(model, Path.Template.LOGIN);
    };

    public static Route handleLoginPost = (Request request, Response response) -> {
        String email = getQueryEmail(request);
        String password = getQueryPassword(request);
        if (authenticateAdmin(email, password)) {
            setLoginAttributes(request.session(), email, LoggedInRole.ADMIN);
            response.redirect(Path.Web.ADMIN_HOME);
            return null;
        } else if(authenticateStudent(email,password)){
            setLoginAttributes(request.session(), email, LoggedInRole.STUDENT);
            response.redirect(Path.Web.STUDENT_HOME);
            return null;
        } else if(authenticateVFUSamordnare(email,password)){
            setLoginAttributes(request.session(), email, LoggedInRole.VFU_SAMORDNARE);
            response.redirect(Path.Web.VFU_SAMORDNARE_HOME);
            return null;
        } else if(authenticateHandledare(email,password)){
            setLoginAttributes(request.session(), email, LoggedInRole.HANDLEDARE);
            response.redirect(Path.Web.HANDLEDARE_HOME);
            return null;
        } else {
            Map<String, Object> model = new HashMap<>();
            model.put("page_title", "VFU-portal SOCIONOM");
            model.put("text_title", "Logga in");
            model.put("login_failed", true);
            model.put("email_input", email);
            return render(model, Path.Template.LOGIN);
        }

    };

    public static Route handleLogoutPost = (Request request, Response response) -> {
        if (isLoggedIn(request)) {
            request.session().removeAttribute(ATTR_ROLE);
            request.session().removeAttribute(ATTR_NAME);
            response.redirect(Path.Web.LOGIN);
            return null;
        }
        response.redirect(Path.Web.HOME);
        return null;
    };

    public static boolean isLoggedIn(Request request) {
        return request.session().attribute(ATTR_ROLE) != null;
    }

    public static boolean isAdmin(Request request) {
        return isRole(LoggedInRole.ADMIN, request);
    }

    public static boolean isStudent(Request request) {
        return isRole(LoggedInRole.STUDENT, request);
    }

    public static boolean isHandledare(Request request) {
        return isRole(LoggedInRole.HANDLEDARE, request);
    }

    public static boolean isVfuSamordnare(Request request) {
        return isRole(LoggedInRole.VFU_SAMORDNARE, request);
    }

    private static boolean isRole(LoggedInRole role, Request request) {
        Object roleAttribute = request.session().attribute(ATTR_ROLE);
        return roleAttribute != null && roleAttribute.equals(role);
    }

    private static String getQueryEmail(Request request) {
        return request.queryParams("email");
    }

    private static String getQueryPassword(Request request) {
        return request.queryParams("password");
    }

    public static void setLoginAttributes(Session session, String email, LoggedInRole role) {
        session.attribute(ATTR_ROLE, role);
        session.attribute(ATTR_NAME, email);
    }

}
