package controllers;

import helpers.SecurityFilter;
import models.FacebookPost;
import models.PostComment;
import models.User;
import play.data.Form;
import play.data.validation.Constraints;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Created by dastko on 8/24/15.
 */
@Security.Authenticated(SecurityFilter.class)
public class PostCommentCtrl extends Controller {

    public Result addComment() {
        Form<CommentForm> commentForm = Form.form(CommentForm.class).bindFromRequest();

        if (commentForm.hasErrors()) {
            return badRequest(commentForm.errorsAsJson());
        } else {
            PostComment newComment = new PostComment();
            FacebookPost facebookPost = FacebookPost.findBlogPostById(commentForm.get().postId);
            facebookPost.save();
            newComment.facebookPost = facebookPost;
            newComment.content = commentForm.get().comment;
            newComment.user = getUser();
            newComment.save();
            return ok(SignupCtrl.buildJsonResponse("success", "Comment added successfully"));
        }
    }

    public static class CommentForm {

        @Constraints.Required
        public Long postId;
        @Constraints.Required
        public String comment;
    }

    private static User getUser() {
        return User.findByEmail(session().get("username"));
    }

}