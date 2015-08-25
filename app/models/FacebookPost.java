package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import play.data.format.Formats;
import play.data.validation.Constraints;
import sun.util.calendar.LocalGregorianCalendar;

import javax.persistence.*;
import java.util.Date;
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
    @Constraints.Min(1)
    public Integer likes;
    @Formats.DateTime(pattern="dd/MM/yyyy")
    @Column(columnDefinition = "datetime")
    public Date date = new Date();


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

    public void increaseLikes(){
        this.likes += 1;
    }
}
