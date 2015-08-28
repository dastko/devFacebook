package controllers;

import helpers.SecurityFilter;
import helpers.SessionHelper;
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
import java.util.HashSet;
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
        System.out.print("Users size:" + UserCtrl.users.size());
        return ok(Json.toJson(getFriends()));
    }


    public Result getOnlineUsers(){
        User user = SessionHelper.currentUser(ctx());
        if(!UserCtrl.users.contains(user)){
            UserCtrl.users.add(user);
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print("Users size:" + UserCtrl.users.size());
        return ok(Json.toJson(findDuplicates(UserCtrl.users, getFriends())));
    }

    public List <User> getFriends(){
        List <Friendship> friendships = Friendship.findAll();
        List <User>onlineFriends = new ArrayList<>();
        Iterator iterator = friendships.iterator();
        while (iterator.hasNext()) {
            Friendship friendship = (Friendship) iterator.next();
            if (friendship.getFriendRequester().getId() != getUser().getId()) {
                onlineFriends.add(friendship.getFriendRequester());
            } else {
                onlineFriends.add(friendship.getFriendAccepter());
            }
        }
        return onlineFriends;
    }

    public List <User> findDuplicates(List <User> allUsers, List <User> allFriends) {
        HashSet<User> map = new HashSet<>();
        List<User> onlineU = new ArrayList<>();
        for (User i : allUsers)
            map.add(i);
        for (User i : allFriends) {
            if (map.contains(i)) {
                onlineU.add(i);
            }
        }
        return onlineU;
    }
}