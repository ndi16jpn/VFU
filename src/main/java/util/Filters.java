package util;

import spark.Filter;
import spark.Request;
import spark.Response;

/**
 * Spark web filters, som t.ex. kan köras innan varje request
 */
public class Filters {

    public static Filter addTrailingSlashes = (Request request, Response response) -> {
        if (!request.pathInfo().endsWith("/")) {
            response.redirect(request.pathInfo() + "/");
        }
    };

    public static Filter setSessionTimeout = (Request request, Response response) -> {
        final int TEN_HOURS_IN_SECONDS = 60*60*10;
        request.session().maxInactiveInterval(TEN_HOURS_IN_SECONDS);
    };

}
