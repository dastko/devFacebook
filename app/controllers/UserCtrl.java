package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import helpers.SessionHelper;
import models.Friendship;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dastko on 8/25/15.
 */
public class UserCtrl extends Controller {

    final static Logger logger = LoggerFactory.getLogger(FacebookPostCtrl.class);

    public static final String AUTH_TOKEN = "authToken";
    public static List <User> users = new ArrayList<>();

    public Result addFriend(long id) {
        Friendship friendship = new Friendship(SessionHelper.currentUser(ctx()), User.findById(id));
        friendship.save();
        return ok(SignupCtrl.buildJsonResponse("Success", "User Succ Add"));
    }

    public Result login() {
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
            if(users.contains(user)){
                users.remove(user);
            }
            users.add(user);
            String authToken = user.createToken();
            ObjectNode authTokenJson = Json.newObject();
            authTokenJson.put(AUTH_TOKEN, authToken);
            response().setCookie(AUTH_TOKEN, authToken);
            return ok(Json.toJson(user));
        }
    }

    public Result logout() {
        users.remove(SessionHelper.currentUser(ctx()));
        session().clear();
        response().discardCookie(AUTH_TOKEN);
        return ok(SignupCtrl.buildJsonResponse("success", "Logged out successfully"));
    }

    public Result isAuthenticated() {
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

    public static User getUser() {
        return (User) Http.Context.current().args.get("username");
    }

    public Result getUsers(){
        return ok(Json.toJson(User.findAll()));
    }


}
