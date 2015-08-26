package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    public Date birthday;
    @Constraints.MaxLength(20)
    public String adress;
    @Constraints.MaxLength(20)
    public String gender;
    @ManyToMany
    @JoinTable(name="facebook_friends",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="friend_id"))
    private List <User> friends;
    @ManyToMany
    @JoinTable(name="facebook_friends",
            joinColumns=@JoinColumn(name="friend_id"),
            inverseJoinColumns=@JoinColumn(name="user_id"))
    private List<User> friendOf;

    private String token;

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
        try {
            return find
                    .where()
                    .eq("email", email.toLowerCase())
                    .eq("encryptedPassword", getSha512(password))
                    .findUnique();
        } catch (Exception e) {
            return null;
        }
    }

    public static User findByEmail(String email) {
        try {
            return find
                    .where()
                    .eq("email", email.toLowerCase())
                    .findUnique();
        } catch (Exception e){
            return null;
        }
    }

    public static List <User> findAll(){
        try {
            return find.findList();
        } catch (Exception e){
            return null;
        }
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
        try {
            User user = find.byId(user_id);
            if (!user.friends.contains(friend)) {
                user.friends.add(friend);
            }
            user.save();
        } catch (Exception e){
            throw e;
        }
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
            return null;
        }
        try  {
            return find.where().eq("token", authToken).findUnique();
        }
        catch (Exception e) {
            return null;
        }
    }
}