package contact;

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
 * Hanterar alla requests för kontakt på sidan
 */
public class ContactController {

    public static Route serveContactPage = (Request request, Response response) -> {
        if (isLoggedIn(request)) {
            redirectToRoleHome(request, response);
            return null;
        }

        Map<String, Object> model = new HashMap<>();
        model.put("page_title", "VFU-portal SOCIONOM");
        return render(model, Path.Template.CONTACT);
    };

}
