package controllers;

import models.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by dastko on 8/26/15.
 */
public class ActiveUsers extends Controller {

    public Result countUser () {
        User user = User.findByEmail("dastko@gmail.com");
        if(!UserCtrl.users.contains(user)){
            UserCtrl.users.add(user);
        }
        return ok(Json.toJson(UserCtrl.users));
    }
}
