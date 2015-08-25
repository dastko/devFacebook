package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.User;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by dastko on 8/25/15.
 */
public class UserCtrl extends Controller {

    public Result addFriend(long id, User friend) {
        User.addFriend(id, friend);
        return ok();
    }

    public static Result login() {
        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(loginForm.errorsAsJson());
        }
        Login loggingInUser = loginForm.get();
        User user = User.findByEmailAndPassword(loggingInUser.email, loggingInUser.password);
        if(user == null) {
            return badRequest(SignupCtrl.buildJsonResponse("error", "Incorrect email or password"));
        } else {
            session().clear();
            session("username", loggingInUser.email);

            ObjectNode wrapper = Json.newObject();
            ObjectNode msg = Json.newObject();
            msg.put("message", "Logged in successfully");
            msg.put("user", loggingInUser.email);
            wrapper.replace("success", msg);
            return ok(wrapper);
        }
    }


    public static Result logout() {
        session().clear();
        return ok(SignupCtrl.buildJsonResponse("success", "Logged out successfully"));
    }

    public static Result isAuthenticated() {
        if(session().get("username") == null) {
            return unauthorized();
        } else {
            ObjectNode wrapper = Json.newObject();
            ObjectNode msg = Json.newObject();
            msg.put("message", "User is logged in already");
            msg.put("user", session().get("username"));
            wrapper.replace("success", msg);
            return ok(wrapper);
        }
    }

    public static class Login {
        @Constraints.Required
        public String password;
        @Constraints.Required
        @Constraints.Email
        public String email;
    }
}
