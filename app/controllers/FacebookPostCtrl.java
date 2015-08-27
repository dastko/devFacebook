package controllers;

import helpers.SecurityFilter;
import models.FacebookPost;
import models.Friendship;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by dastko on 8/24/15.
 */
public class FacebookPostCtrl extends Controller {

    final static Logger logger = LoggerFactory.getLogger(FacebookPostCtrl.class);


    @Security.Authenticated(SecurityFilter.class)
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

    public static User getUser() {
        return User.findByEmail(session().get("username"));
    }

    public Result getPosts(){
        return ok(Json.toJson(FacebookPost.findAll()));
    }

    public Result getUserFriendList(){

        List <Friendship> friendships = Friendship.findAll();
        List <User> friends = new ArrayList<>();

        Iterator iterator = friendships.iterator();
        while (iterator.hasNext()) {
            Friendship friendship = (Friendship) iterator.next();
            if (friendship.getFriendRequester().getId() != getUser().getId()) {
                friends.add(friendship.getFriendRequester());
            } else {
                friends.add(friendship.getFriendAccepter());
            }
        }

        return ok(Json.toJson(friends));
    }
}