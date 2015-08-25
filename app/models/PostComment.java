package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.List;

/**
 * Created by dastko on 8/24/15.
 */
@Entity
public class PostComment extends Model {

    @Id
    public Long id;
    @ManyToOne
    @JsonIgnore
    public FacebookPost facebookPost;
    @ManyToOne
    public User user;
    @Column(columnDefinition = "TEXT")
    public String content;

    public static final Model.Finder<Long, PostComment> find = new Model.Finder<>(
            PostComment.class);

    public static List<PostComment> findAllCommentsByPost(final FacebookPost facebookPost) {
        return find
                .where()
                .eq("post", facebookPost)
                .findList();
    }

    public static List<PostComment> findAllCommentsByUser(final User user) {
        return find
                .where()
                .eq("user", user)
                .findList();
    }
}
