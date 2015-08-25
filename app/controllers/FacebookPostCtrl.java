package controllers;

import models.FacebookPost;
import models.User;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.*;

/**
 * Created by dastko on 8/24/15.
 */
public class FacebookPostCtrl extends Controller {

    @Security.Authenticated(helpers.Security.class)
    public Result addPost() {
        Form<PostForm> postForm = Form.form(PostForm.class).bindFromRequest();

        if (postForm.hasErrors()) {
            return badRequest(postForm.errorsAsJson());
        } else {
            FacebookPost newPost = new FacebookPost();
            newPost.setContent(postForm.get().content);
            newPost.user = getUser();
            newPost.save();
        }
        return ok(SignupCtrl.buildJsonResponse("success", "Post add successfully"));
    }

    public static class PostForm {
        @Constraints.Required
        public String content;
    }

    private static User getUser() {
        return User.findByEmail(session().get("username"));
    }

    @Security.Authenticated(helpers.Security.class)
    public Result getPosts(){
        return ok(Json.toJson(FacebookPost.findAll()));
    }
}
