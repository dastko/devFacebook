package controllers;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by dastko on 8/26/15.
 */
public class ActiveUsers extends Controller {

    public Result countUser () {
        return ok(Json.toJson(UserCtrl.users));
    }
}
