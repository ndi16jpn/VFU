package handledare;

import database.Database;
import database.DatabaseHandler;
import organisations.Place;
import roles.LoggedInRole;
import spark.Request;
import spark.Response;
import spark.Route;
import util.Path;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            model.put("html_content", DatabaseHandler.getDatabase().getSelector().getHandledarePageHtml());

                if (place.getStudent() == null){
                model.put("contact_student_name", "Ingen tilldelad student");
                model.put("contact_student_email", "Ingen tilldelad student");
                model.put("contact_student_phone_number", "Ingen tilldelad student");
            }else{
                model.put("contact_student_name", place.getStudent().getStudentData().getName());
                model.put("contact_student_email", place.getStudent().getStudentData().getEmail());
                model.put("contact_student_phone_number", place.getStudent().getStudentData().getPhoneNumber());
            }

            String uploadPath = new File(".").getCanonicalPath() + File.separator + Path.Directories.FILE_DIRECTORY;

            File fileUploadDirectory = new File(uploadPath);
            if (!fileUploadDirectory.exists()) {
                fileUploadDirectory.mkdirs();
            }
            File[] allFiles = fileUploadDirectory.listFiles();
            List<String> filenames = new ArrayList<>();
            for (File file : allFiles) {
                filenames.add(file.getName());
            }
            //model.put("file_path", Path.Directories.FILE_DIRECTORY + File.separator);
            model.put("files", filenames);

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
    public static Route handleHandledareDownloadHandledareFile = (Request request, Response response) -> {
        if(isHandledare(request)) {
            String fileName = request.queryParams("fileToDownload");
            String path = new File(".").getCanonicalPath() + File.separator + Path.Directories.FILE_DIRECTORY + File.separator;
            String filePath = path + fileName;
            File file = new File(filePath);
            OutputStream outputStream = null;
            FileInputStream inputStream = null;
            if (file.exists()) {
                String headerKey = "Content-Disposition";
                String headerValue = String.format("attachment; filename=\"%s\"", file.getName());
                outputStream = response.raw().getOutputStream();
                response.raw().setHeader(headerKey, headerValue);
                inputStream = new FileInputStream(file);

                byte[] buffer = new byte[4096];
                int length;
                while ((length = inputStream.read(buffer)) > 0){
                    outputStream.write(buffer, 0, length);
                }
                inputStream.close();
                outputStream.flush();
                return serveHandledareHomePage.handle(request, response);

            }
            return "";
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

}
