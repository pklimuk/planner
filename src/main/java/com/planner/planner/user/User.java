package com.planner.planner.user;

import com.planner.planner.deadline.Deadline;
import com.planner.planner.event.Event;
import com.planner.planner.group.Group;
import com.planner.planner.userProfile.UserProfile;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
@Entity(name = "Users")
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "user_login_unique", columnNames = "login")
        }
)
public class User implements UserDetails {

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

    @Enumerated(EnumType.STRING)
    private UserRole userRole = UserRole.USER;
    private Boolean locked = false;

    // TODO: Change user parameter to FALSE after development tests
    private Boolean enabled = true;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "user_profile_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "user_profile_id_fk"
            )
    )
    private UserProfile user_profile;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(
            mappedBy = "event_user",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<Event> events = new ArrayList<>();

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(
            mappedBy = "deadline_user",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<Deadline> deadlines = new ArrayList<>();

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(
            mappedBy = "group_user",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<Group> groups = new ArrayList<>();

    public User(String login, String password, UserRole userRole) {
        this.login = login;
        this.password = password;
        this.userRole = userRole;
    }

    public User(String login, String password, UserProfile user_profile) {
        this.login = login;
        this.password = password;
        this.user_profile = user_profile;
    }

    public User(String login, String password, UserRole userRole, UserProfile user_profile) {
        this.login = login;
        this.password = password;
        this.user_profile = user_profile;
        this.userRole = userRole;
    }

    public User() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(userRole.name());
        return Collections.singletonList(authority);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
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

    public UserProfile getUser_profile() {
        return user_profile;
    }

    public void setUser_profile(UserProfile user_profile) {
        this.user_profile = user_profile;
    }

    public List<Deadline> getDeadlines() {
        return deadlines;
    }

    public void addGroup(Group group) {
        if (!this.groups.contains(group)) {
            this.groups.add(group);
            group.setUser(this);
        }
    }

    public void removeGroup(Group group) {
        if (this.groups.contains(groups)) {
            this.groups.remove(group);
            group.setUser(null);
        }
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
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
