package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import controllers.FacebookPostCtrl;
import controllers.UserCtrl;
import play.data.format.Formats;
import play.data.validation.Constraints;
import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User extends Model {

    private static final String USER ="user";

    @Id
    public Long id;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


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
    @Formats.DateTime(pattern = "dd/MM/yyyy")
    @Column(columnDefinition = "datetime")
    public Date registration = new Date();
    @Formats.DateTime(pattern = "dd/MM/yyyy")
    @Column(columnDefinition = "datetime")
    public Date birthday;
    @Constraints.MaxLength(20)
    public String adress;
    @Constraints.MaxLength(20)
    public String gender;

    private String token;
    public String role = USER;


    public void setPassword(String password) {
        this.encryptedPassword = getSha512(password);
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public String getEmail() {
        return email;
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

    public static List<User> findAll() {
        return find.findList();
    }

    public static User findById(long id){
        return find.byId(id);
    }

    public static byte[] getSha512(String value) {
        try {
            return MessageDigest.getInstance("SHA-512").digest(value.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addFriend(long user_id, User friend) {
        User user = find.byId(user_id);
//        if (!user.friends.contains(friend) && user_id != friend.id && !friend.friends.contains(user)) {
//            user.friends.add(friend);
//        }
        user.save();
        friend.save();
        // Ukoliko korisnik potvrdi zahtjev za prijateljstvom
    }

    public String createToken() {
        token = UUID.randomUUID().toString();
        save();
        return token;
    }

    public void deleteToken() {
        token = null;
        save();
    }

    public static User findByAuthToken(String authToken) {
        if (authToken == null) {
        }
        return find.where().eq("token", authToken).findUnique();
    }

    public static List<User> getFriendsList() {
        User u = FacebookPostCtrl.getUser();
        return null;
    }
}