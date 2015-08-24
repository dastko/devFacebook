package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.User;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.*;

public class SignupCtrl extends Controller {

    public Result index(){
        return ok("Sucess");
    }

    public Result signup() {
        Form<SignUp> signUpForm = Form.form(SignUp.class).bindFromRequest();

        if ( signUpForm.hasErrors()) {
            return badRequest(signUpForm.errorsAsJson());
        }
        SignUp newUser =  signUpForm.get();
        User existingUser = User.findByEmail(newUser.email);
        if(existingUser != null) {
            return badRequest(buildJsonResponse("error", "User exists"));
        } else {
            User user = new User();
            user.setEmail(newUser.email);
            user.setPassword(newUser.password);
            user.save();
            session().clear();
            session("username", newUser.email);
            return ok(buildJsonResponse("success", "User created successfully"));
        }
    }

    public static Result logout() {
        session().clear();
        return ok(buildJsonResponse("success", "Logged out successfully"));
    }

    public static class SignUp {
        @Constraints.Required
        @Constraints.Email
        public String email;
        @Constraints.Required
        @Constraints.MinLength(6)
        public String password;

    }

    public static ObjectNode buildJsonResponse(String type, String message) {
        ObjectNode wrapper = Json.newObject();
        ObjectNode msg = Json.newObject();
        msg.put("message", message);
        wrapper.replace(type, msg);
        return wrapper;
    }
}
