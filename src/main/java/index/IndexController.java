package index;

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
 * Hanterar alla requests för inloggad på sidan
 */
public class IndexController {

    public static Route serveIndexPage = (Request request, Response response) -> {
        if (isLoggedIn(request)) {
            redirectToRoleHome(request, response);
            return null;
        }

        Map<String, Object> model = new HashMap<>();
        model.put("page_title", "VFU-portal SOCIONOM");
        return render(model, Path.Template.INDEX);
    };

}
