package controllers;

import helpers.SecurityFilter;
import models.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Created by dastko on 8/26/15.
 */
public class ActiveUsers extends Controller {

    @Security.Authenticated(SecurityFilter.class)
    public Result countUser () {
        User user = User.findByEmail(session().get("username"));
        if(UserCtrl.users.contains(user)){
            UserCtrl.users.remove(user);
        }
        UserCtrl.users.add(user);
        return ok(Json.toJson(UserCtrl.users));
    }
}
