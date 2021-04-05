package com.planner.planner.user;

import com.planner.planner.deadline.Deadline;
import com.planner.planner.event.Event;
import com.planner.planner.userProfile.UserProfile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Users")
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "user_login_unique", columnNames = "login")
        }
)
public class User {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )

    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "login",
            nullable = false
    )
    private String login;

    @Column(
            name = "password",
            nullable = false
    )
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "user_profile_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "user_profile_id_fk"
            )
    )
    private UserProfile user_profile;

    @OneToMany(
            mappedBy = "event_user",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<Event> events = new ArrayList<>();

    @OneToMany(
            mappedBy = "deadline_user",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<Deadline> deadlines = new ArrayList<>();

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User(String login, String password, UserProfile user_profile) {
        this.login = login;
        this.password = password;
        this.user_profile = user_profile;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addEvent(Event event) {
        if (!this.events.contains(event)) {
            this.events.add(event);
            event.setUser(this);
        }
    }

    public void removeEvent(Event event) {
        if (this.events.contains(event)) {
            this.events.remove(event);
            event.setUser(null);
        }
    }

//    public void setUser(User user) {
//        this.user = studentIdCard;
//    }

    public List<Event> getEvents() {
        return events;
    }

    public void addDeadline(Deadline deadline) {
        if (!this.deadlines.contains(deadline)) {
            this.deadlines.add(deadline);
            deadline.setUser(this);
        }
    }

    public void removeDeadline(Deadline deadline) {
        if (this.deadlines.contains(deadline)) {
            this.deadlines.remove(deadline);
            deadline.setUser(null);
        }
    }

//    public void setUser(User user) {
//        this.user = studentIdCard;
//    }

    public List<Deadline> getDeadlines() {
        return deadlines;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", userProfile=" + user_profile +
                '}';
    }
}
