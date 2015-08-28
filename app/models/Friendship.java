package models;

import com.avaje.ebean.Model;
import play.data.format.Formats;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by dastko on 8/27/15.
 */
@Entity
public class Friendship extends Model {
    @Id
    public Long id;

    public Long getId() {
        return id;
    }

    @ManyToOne
    @JoinColumn(name="friend_requester")
    public User friendRequester;

    public User getFriendRequester() {
        return friendRequester;
    }

    public User getFriendAccepter() {
        return friendAccepter;
    }

    @ManyToOne
    @JoinColumn(name="friend_accepter")
    public User friendAccepter;
    @Formats.DateTime(pattern = "dd/MM/yyyy")
    @Column(columnDefinition = "datetime")
    public Date registration = new Date();


    public static final Model.Finder<Long, Friendship> find = new Model.Finder<>(
            Friendship.class);

    public Friendship(User friendRequester, User friendAccepter){
        this.friendRequester = friendRequester;
        this.friendAccepter = friendAccepter;
    }

    public static List <Friendship> findAll(){
        return find.findList();
    }
}