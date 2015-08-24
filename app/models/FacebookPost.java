package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.List;

/**
 * Created by dastko on 8/24/15.
 */
@Entity
public class FacebookPost extends Model {

    @Id
    public Long id;
    @Column(columnDefinition = "TEXT")
    @Constraints.Required
    public String content;
    @ManyToOne
    public User user;
    @OneToMany(cascade = CascadeType.ALL)
    public List<PostComment> comments;


    public static final Model.Finder<Long, FacebookPost> find = new Model.Finder<>(
            FacebookPost.class);

    public static List<FacebookPost> findBlogPostsByUser(final User user) {
        return find
                .where()
                .eq("user", user)
                .findList();
    }

    public static FacebookPost findBlogPostById(final Long id) {
        return find
                .where()
                .eq("id", id)
                .findUnique();
    }

    public static List <FacebookPost> findAll(){
        return find.findList();
    }

    public void setContent(String content) {
        this.content = content;
    }


}
