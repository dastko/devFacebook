package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Entity
public class User extends Model {

    @Id
    public Long id;
    @Column(length = 255, nullable = false, unique = true)
    @Constraints.MaxLength(255)
    @Constraints.Required
    @Constraints.Email
    public String email;
    @Column(length = 64, nullable = false)
    private byte[] encryptedPassword;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    public List<FacebookPost> facebookPostList;

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
}