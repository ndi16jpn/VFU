package registration;

import database.Database;
import database.DatabaseHandler;
import login.LoginController;
import roles.LoggedInRole;
import roles.VFUSamordnare;
import security.PasswordSecurity;
import spark.Request;
import spark.Response;
import spark.Route;
import util.Path;

import java.util.HashMap;
import java.util.Map;

import static login.LoginController.*;
import static util.RedirectUtil.redirectToRoleHome;
import static util.ViewUtil.render;

/**
 * Hanterare web requests för registreringssida
 */
public class RegistrationController {

    public static Route serveRegVfuSam = (Request request, Response response) -> {
        if (isLoggedIn(request)) {
            redirectToRoleHome(request, response);
            return null;
        }
        Map<String, Object> model = new HashMap<>();
        model.put("page_title", "VFU-portal SOCIONOM");

        String regLink = getQueryRegLinkKey(request);
        Database db = DatabaseHandler.getDatabase();
        String email = db.getSelector().getVfuEmailFromRegLink(regLink);
        if (email == null) {
            return render(model, Path.Template.REG_INVALID_LINK);
        }

        String errorMsg = request.attribute("error_msg");
        if (errorMsg != null) {
            model.put("error", true);
            model.put("error_msg", errorMsg);
        }

        VFUSamordnare vfuSamordnare = db.getSelector().getVFUSamordnare(email);
        model.put("vfu_name", vfuSamordnare.getName());
        model.put("vfu_email", email);
        model.put("vfu_phone", vfuSamordnare.getPhoneNumber());
        model.put("vfu_region", vfuSamordnare.getRegion());
        model.put("reg_link", regLink);

        return render(model, Path.Template.REG_VFU_SAMORDNARE);
    };

    public static Route handleRegVfuSam = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        model.put("page_title", "VFU-portal SOCIONOM");

        String regLink = getQueryRegLinkKey(request);
        Database db = DatabaseHandler.getDatabase();
        String email = db.getSelector().getVfuEmailFromRegLink(regLink);
        if (email == null) {
            return render(model, Path.Template.INDEX);
        }

        String password = getQueryPassword(request);
        String passwordAgain = getQueryPasswordAgain(request);
        if (!password.equals(passwordAgain)) {
            request.attribute("error_msg", "Lösenorden matchade inte");
            return serveRegVfuSam.handle(request, response);
        } else if (password.length() < 6) {
            request.attribute("error_msg", "Lösenordet var för kort");
            return serveRegVfuSam.handle(request, response);
        }

        db.getInserter().setVfuSamordnarePassword(email, PasswordSecurity.createHash(password.toCharArray()));
        db.getDeleter().deleteLinkMailVFUContent(regLink);

        LoginController.setLoginAttributes(request.session(), email, LoggedInRole.VFU_SAMORDNARE);
        response.redirect(Path.Web.VFU_SAMORDNARE_HOME);
        return null;
    };

    private static String getQueryRegLinkKey(Request request) {
        return request.params(":regLink");
    }

    private static String getQueryPassword(Request request) {
        return request.queryParams("password");
    }

    private static String getQueryPasswordAgain(Request request) {
        return request.queryParams("password_again");
    }

}
