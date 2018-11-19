package util;

import spark.Request;
import spark.Response;

import static login.LoginController.*;

/**
 * Redirektar användaren till sin hemsida om inloggad
 */
public class RedirectUtil {

    public static void redirectToRoleHome(Request request, Response response) {
        if (isAdmin(request)) {
            response.redirect(Path.Web.ADMIN_HOME);
        } else if (isStudent(request)) {
            response.redirect(Path.Web.STUDENT_HOME);
        } else if (isHandledare(request)) {
            response.redirect(Path.Web.HANDLEDARE_HOME);
        } else if (isVfuSamordnare(request)) {
            response.redirect(Path.Web.VFU_SAMORDNARE_HOME);
        }
    }

}
