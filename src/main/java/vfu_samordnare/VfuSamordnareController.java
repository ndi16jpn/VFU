package vfu_samordnare;

import auth.RandomGenerator;
import database.Database;
import database.DatabaseHandler;
import email.MailSender;
import email.MailSenderProvider;
import organisations.Place;
import organisations.Unit;
import roles.Handledare;
import roles.LoggedInRole;
import security.PasswordSecurity;
import spark.Request;
import spark.Response;
import spark.Route;
import util.Path;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static login.LoginController.*;
import static util.ViewUtil.render;

/**
 * Hanterar alla request för vfu samordnare på sidan
 */
public class VfuSamordnareController {

    public static Route serveVfuSamordnareHomePage = (Request request, Response response) -> {
        if (isVfuSamordnare(request)) {
            Map<String, Object> model = new HashMap<>();
            model.put("page_title", "VFU-portal SOCIONOM");
            model.put("text_title", "Välkommen VFU Samordnare");
            model.put("text_content", "Välkommen " + request.session().attribute(ATTR_NAME) + "!"
                   );
            model.put("home_link", Path.Web.VFU_SAMORDNARE_HOME);
            model.put(ATTR_ROLE, LoggedInRole.VFU_SAMORDNARE.getRoleName());
            model.put(ATTR_NAME, request.session().attribute(ATTR_NAME));
            model.put("html_content", DatabaseHandler.getDatabase().getSelector().getVFUSamordePageHTML());
            return render(model, Path.Template.VFU_SAMORDNARE_HOME);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };
    public static Route serveVfuSamordnareAddPlacePage = (Request request, Response response) -> {
        if (isVfuSamordnare(request)) {
            Map<String, Object> model = new HashMap<>();
            model.put("text_content", "...");
            model.put("home_link", Path.Web.VFU_SAMORDNARE_HOME);
            model.put(ATTR_ROLE, LoggedInRole.VFU_SAMORDNARE.getRoleName());
            model.put(ATTR_NAME, request.session().attribute(ATTR_NAME));
            Database db = DatabaseHandler.getDatabase();
            List<String> muni = db.getSelector().getAllMunicipalitiesForRegion
                    (db.getSelector().getRegionFromVfuSamEmail(request.session().attribute(ATTR_NAME)).getName());
            String region = db.getSelector().getRegionFromVfuSamEmail(request.session().attribute(ATTR_NAME)).getName();
            model.put("muni_list", muni);
            List<Unit> units = db.getSelector().getAllUnitFromRegion((region));

            if(request.attribute("errDel") != null){
                model.put("cantDelete", "Du kan inte ta bort en enhet som har en student eller en enhet som en student har som ett val.");
            }else if(request.attribute("errAdd") != null){
                model.put("cantDelete", "Den här platsen finns redan på den här kommunen");
            }else if(request.attribute("errDelPlaceStud") != null){
                model.put("cantDelete", "Den här platsen har en student");
            }
            List<Place> places = db.getSelector().getAllPlaces();

            List<Place> filteredPlaces = places.stream().filter(place -> place.getUnit().getMunicipality().getRegions().stream()
                    .map(region1 -> region1.getName()).collect(Collectors.toList()).contains(region)).collect(Collectors.toList());

            model.put("filtered_list", filteredPlaces);
            model.put("unit_list", units);
            model.put("place_id", places);


            return render(model, Path.Template.VFU_SAMORDNARE_ADD_PLACE);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };
    public static Route serveVfuSamordnarePlaceListPage = (Request request, Response response) -> {
        if (isVfuSamordnare(request)) {
            Map<String, Object> model = new HashMap<>();
            model.put("text_content", "...");
            model.put("home_link", Path.Web.VFU_SAMORDNARE_HOME);
            model.put(ATTR_ROLE, LoggedInRole.VFU_SAMORDNARE.getRoleName());
            model.put(ATTR_NAME, request.session().attribute(ATTR_NAME));
            Database db = DatabaseHandler.getDatabase();

            String region = db.getSelector().getRegionFromVfuSamEmail(request.session().attribute(ATTR_NAME)).getName();
            List<Unit> units = db.getSelector().getAllUnitFromRegion((region));
            List<Place> places = db.getSelector().getAllPlaces();

            List<Place> filteredPlaces = places.stream().filter(place -> place.getUnit().getMunicipality().getRegions().stream()
                    .map(region1 -> region1.getName()).collect(Collectors.toList()).contains(region)).collect(Collectors.toList());

            model.put("filtered_list", filteredPlaces);
            model.put("unit_list", units);
            model.put("place_id", places);

            return render(model, Path.Template.VFU_SAMORDNARE_PLACE_LIST);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };
    public static Route handleVfuSamordnareAddSinglePost = (Request request, Response response) -> {
        if (isVfuSamordnare(request)) {
            Map<String, Object> model = new HashMap<>();

            String unitName = getQueryUnitName(request);
            String unitNumberOfSlots = getQueryUnitNumberOfSlots(request);
            String unitMunicipality = getQueryUnitMunicipality(request);
            String unitInfo = getQueryUnitInfo(request);
            Database db = DatabaseHandler.getDatabase();
            model.put("muni_list", DatabaseHandler.getDatabase().getSelector().getAllMunicipalitiesForRegion
                    (DatabaseHandler.getDatabase().getSelector().getRegionFromVfuSamEmail(request.session().attribute(ATTR_NAME)).getName()));
            for(Unit unit : db.getSelector().getAllUnits()) {
                if (unit.getName().equals(unitName) && unit.getMunicipality().getName().equals(unitMunicipality)) {
                    request.attribute("errAdd", true);
                    return serveVfuSamordnareAddPlacePage.handle(request, response);
                }
            }
            Unit unit = new Unit(db.getSelector().getMunicipality(unitMunicipality), unitName ,Integer.valueOf(unitNumberOfSlots),unitInfo);

            db.getInserter().addUnit(unit);
            db.getInserter().addPlace(new Place(db.getSelector().getUnit(db.getSelector().getMunicipality(unitMunicipality),unitName), false));
            return serveVfuSamordnareAddPlacePage.handle(request, response);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };
    public static Route handleVfuSamordnareAddHandledarePost = (Request request, Response response) -> {
        if (isVfuSamordnare(request)) {
            Map<String, Object> model = new HashMap<>();
            String handledareEmail = getQueryHandledareEmail(request);
            String handledareName = getQueryHandledareName(request);
            String handledarePhoneNumber = getQueryHandledarePhoneNumber(request);
            int id = Integer.valueOf(getQueryPlaceId(request));
            Database db = DatabaseHandler.getDatabase();
            model.put("handledare_email", handledareEmail);

            //TODO: Reg-mail innan handledare skapas?
            //Default password Handledare = Vfusocionom1
            Handledare handledare;
            if(db.getSelector().handledareExistsOnPlace(handledareEmail)){
                //TODO: Varningstext om det redan finns en handledare! Inte bara returnera samma sida.
                return serveVfuSamordnareAddPlacePage.handle(request, response);
            }
            //TODO: Möjligtvis javasqript för att disablea knappen lägg till handledare, istället för if/else
            //TODO: Reg-mail innan handledare skapas?
            //Default password Handledare = Vfusocionom1

            String password = RandomGenerator.generateRandomPassword();
            MailSender sender = MailSenderProvider.getMailSender();


            if(handledareName == null && handledarePhoneNumber == null){
                //handledare = new Handledare(handledareEmail,"1000:abe61a950f20c9f85c486ffd7d58daa4c8a4e0b9e15afd5a:fe1d7f49edbd6ab39559e7270f2f76d9b5bb612f839b66b6");
                handledare = new Handledare(handledareEmail, PasswordSecurity.createHash(password.toCharArray()));
                //sender.sendMail(handledareEmail, "Lösenord till Vfusocionm", "Hej " + handledareName + "<br>" + "Ditt lösenord till "
                //        + "<a href='vfusocionom.hig.se'>" + "Vfusocionom.hig.se " + "</a>" + "<br>" +"Lösenord: " + password);
                db.getInserter().saveHandledareRegistrationMail(handledareEmail,"Hej " + handledareName + "<br>" + "Ditt lösenord till "
                        + "<a href='vfusocionom.hig.se'>" + "Vfusocionom.hig.se" + "</a>" + "<br>" +"Lösenord: " + password);
            }else if(handledarePhoneNumber == null){
                //handledare = new Handledare(handledareEmail,handledareName,"1000:abe61a950f20c9f85c486ffd7d58daa4c8a4e0b9e15afd5a:fe1d7f49edbd6ab39559e7270f2f76d9b5bb612f839b66b6");
                handledare = new Handledare(handledareEmail,handledareName,PasswordSecurity.createHash(password.toCharArray()));
                //sender.sendMail(handledareEmail, "Lösenord till Vfusocionm", "Hej " + handledareName + "<br>" + "Ditt lösenord till "
                //        + "<a href='vfusocionom.hig.se'>" + "Vfusocionom.hig.se" + "</a>" + "<br>" +"Lösenord: " + password);
                db.getInserter().saveHandledareRegistrationMail(handledareEmail,"Hej " + handledareName + "<br>" + "Ditt lösenord till "
                        + "<a href='vfusocionom.hig.se'>" + "Vfusocionom.hig.se" + "</a>" + "<br>" +"Lösenord: " + password);
            }
            else{
                //handledare = new Handledare(handledareName,handledareEmail,handledarePhoneNumber,"1000:abe61a950f20c9f85c486ffd7d58daa4c8a4e0b9e15afd5a:fe1d7f49edbd6ab39559e7270f2f76d9b5bb612f839b66b6");
                handledare = new Handledare(handledareName,handledareEmail,handledarePhoneNumber,PasswordSecurity.createHash(password.toCharArray()));
                //sender.sendMail(handledareEmail, "Lösenord till Vfusocionm", "Hej " + handledareName + "<br>" + "Ditt lösenord till "
                //        + "<a href='vfusocionom.hig.se'>" + "Vfusocionom.hig.se " + "</a>" + "<br>" +"Lösenord: " + password);
                db.getInserter().saveHandledareRegistrationMail(handledareEmail,"Hej " + handledareName + "<br>" + "Ditt lösenord till "
                        + "<a href='vfusocionom.hig.se'>" + "Vfusocionom.hig.se" + "</a>" + "<br>" +"Lösenord: " + password);
            }

            db.getInserter().addHandledare(handledare);
            db.getInserter().addHandledareToPlace(db.getSelector().getPlace(id),db.getSelector().getHandledare(handledareEmail));
            return serveVfuSamordnareAddPlacePage.handle(request, response);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };


    public static Route handleVfuSamordnareDeleteUnitPost = (Request request, Response response) -> {
        if (isVfuSamordnare(request)) {

            String deleteQuery = getQueryUnitToDelete(request);
            String[] parts = deleteQuery.split("@@");
            String name = parts[0];
            String muni = parts[1];
            Database db = DatabaseHandler.getDatabase();
            List<Place> places = db.getSelector().getAllPlacesForUnit(db.getSelector().getUnit(db.getSelector().getMunicipality(muni),name));
            if(db.getSelector().isChosen(db.getSelector().getUnit(db.getSelector().getMunicipality(muni),name).getId())){
                request.attribute("errDel", true);
                return serveVfuSamordnareAddPlacePage.handle(request, response);
            }
            for(Place place : places){
                if(place.getStudent() != null){
                    request.attribute("errDel", true);
                    return serveVfuSamordnareAddPlacePage.handle(request, response);
                }

            }

            db.getDeleter().deleteAllPlacesForUnit(db.getSelector().getUnit(db.getSelector().getMunicipality(muni),name));
            db.getDeleter().deleteUnitContent(db.getSelector().getUnit(db.getSelector().getMunicipality(muni),name));
            return serveVfuSamordnareAddPlacePage.handle(request, response);

        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };
    public static Route handleVfuSamordnareDeleteHandledarePost = (Request request, Response response) -> {
        if (isVfuSamordnare(request)) {

            String handledareToDelete = getQueryHandledareToDelete(request);
            Database db = DatabaseHandler.getDatabase();
            db.getDeleter().deleteHandledareFromPlace(db.getSelector().getHandledare(handledareToDelete));
            db.getDeleter().deleteHandledareContent(db.getSelector().getHandledare(handledareToDelete));

            return serveVfuSamordnareAddPlacePage.handle(request, response);

        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };
    public static Route handleAddSinglePlacePost = (Request request, Response response) -> {
        if (isVfuSamordnare(request)) {

            String unitName = getQueryUnitName(request);
            String unitMunicipality = getQueryUnitMunicipality(request);

            Database db = DatabaseHandler.getDatabase();
            Unit unit = db.getSelector().getUnit(db.getSelector().getMunicipality(unitMunicipality),unitName);
            db.getInserter().addOneNumberOfSlotsUnit(unit);
            db.getInserter().addSinglePlace(new Place(unit, false));
            return serveVfuSamordnareAddPlacePage.handle(request,response);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };
    public static Route handleDeleteSinglePlacePost = (Request request, Response response) -> {
        if (isVfuSamordnare(request)) {

            String unitName = getQueryUnitName(request);
            String unitMunicipality = getQueryUnitMunicipality(request);
            Database db = DatabaseHandler.getDatabase();
            Unit unit = db.getSelector().getUnit(db.getSelector().getMunicipality(unitMunicipality),unitName);

            String placeID = getQueryPlaceId(request);
            Place place = db.getSelector().getPlace(Integer.valueOf(placeID));
            if(place.getStudent() != null){
                request.attribute("errDelPlaceStud", true);
                return serveVfuSamordnareAddPlacePage.handle(request,response);
            }if(place.getHandledare() != null){
                List<Handledare> handledare = db.getSelector().getHandledareForPlace(place.getId());
                for (Handledare hand:  handledare) {
                    db.getDeleter().deleteHandledareFromPlace(hand);
                    db.getDeleter().deleteHandledareContent(hand);
                }


            }
            db.getInserter().deleteOneNumberOfSlotsUnit(unit);
            db.getDeleter().deleteSinglePlace(db.getSelector().getPlace(Integer.valueOf(placeID)));
            return serveVfuSamordnareAddPlacePage.handle(request,response);
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    public static Route handleUpdatePhoneNumberPost = (Request request, Response response) -> {
        if (isVfuSamordnare(request)) {
            String email = getQueryHandledareToUpdate(request);
            String phoneNumber = getQueryPhonenumberToUpdate(request);

            DatabaseHandler.getDatabase().getInserter().updatePhonenumberHandledare(phoneNumber,email);
            response.redirect(Path.Web.VFU_SAMORDNARE_ADD_PLACE);
            return null;
        } else {
            response.redirect(Path.Web.LOGIN);
            return null;
        }
    };

    private static String getQueryPhonenumberToUpdate(Request request) {
        return request.queryParams("phonenumberToUpdate");

    }
    private static String getQueryHandledareToUpdate(Request request) {
        return request.queryParams("handledareToUpdate");
    }

    private static String getQueryUnitName(Request request) {
        return request.queryParams("unitName");
    }


    private static String getQueryUnitToDelete(Request request) {
        return request.queryParams("unitToDelete");
    }

    private static String getQueryHandledareEmail(Request request) {
        return request.queryParams("handledareToAdd");
    }
    private static String getQueryUnitNumberOfSlots(Request request) {
        return request.queryParams("unitNumberOfSlots");
    }
    private static String getQueryHandledareName(Request request) {
        return request.queryParams("handledareName");
    }

    private static String getQueryHandledarePhoneNumber(Request request) {
        return request.queryParams("handledarePhoneNumber");
    }


    private static String getQueryPlaceId(Request request) {
        return request.queryParams("placeId");
    }
    private static String getQueryUnitMunicipality(Request request) {
        return request.queryParams("unitMunicipality");
    }

    private static String getQueryUnitInfo(Request request) {
        return request.queryParams("unitInfo");
    }
    private static String getQueryHandledareToDelete(Request request) {
        return request.queryParams("handledareToDelete");
    }


}
