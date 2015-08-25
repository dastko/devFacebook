package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.format.Formats;
import play.data.validation.Constraints;
import sun.util.calendar.LocalGregorianCalendar;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="facebook_users")
public class User extends Model {

    @Id
    public Long id;
    @Column(unique = true)
    @Constraints.MaxLength(255)
    @Constraints.Required
    @Constraints.Email
    public String email;
    @Constraints.MaxLength(255)
    @Constraints.Required
    public String name;
    @Column(length = 64, nullable = false)
    private byte[] encryptedPassword;
    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    public List<FacebookPost> facebookPostList;
    @Formats.DateTime(pattern="dd/MM/yyyy")
    @Column(columnDefinition = "datetime")
    public Date registration = new Date();
    @Formats.DateTime(pattern="dd/MM/yyyy")
    @Column(columnDefinition = "datetime")
    public Date birthday = new Date();
    @Constraints.MaxLength(20)
    public String adress;
    @Constraints.MaxLength(20)
    public String gender;
    @ManyToMany
    @JoinTable(name="facebook_friends",
            joinColumns=@JoinColumn(name="userId"),
            inverseJoinColumns=@JoinColumn(name="friendId"))
    private List <User> friends;
    @ManyToMany
    @JoinTable(name="facebook_friends",
            joinColumns=@JoinColumn(name="friendId"),
            inverseJoinColumns=@JoinColumn(name="userId"))
    private List<User> friendOf;
    public String token;

    public void setPassword(String password) {
        this.encryptedPassword = getSha512(password);
    }
    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public static final Finder<Long, User> find = new Finder<>(
             User.class);

    public static User findByEmailAndPassword(String email, String password) {
        return find
                .where()
                .eq("email", email.toLowerCase())
                .eq("encryptedPassword", getSha512(password))
                .findUnique();
    }

    public static User findByEmail(String email) {
        return find
                .where()
                .eq("email", email.toLowerCase())
                .findUnique();
    }

    public static byte[] getSha512(String value) {
        try {
            return MessageDigest.getInstance("SHA-512").digest(value.getBytes("UTF-8"));
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addFriend(long user_id, User friend){
        User user = find.byId(user_id);
        if(!user.friends.contains(friend)){
            user.friends.add(friend);
        }
        user.save();
    }
}