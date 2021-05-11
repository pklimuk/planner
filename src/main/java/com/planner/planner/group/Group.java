package com.planner.planner.group;

import com.planner.planner.deadline.Deadline;
import com.planner.planner.event.Event;
import com.planner.planner.user.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "Groups")
@Table(
        name = "groups"
)
public class Group {
    @Id
    @SequenceGenerator(
            name = "group_sequence",
            sequenceName = "group_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "group_sequence"
    )

    @Column(
            name = "group_id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "group_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String group_name;

    @Column(
            name = "group_desription",
            columnDefinition = "TEXT"
    )
    private String group_description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "event_group",
            joinColumns = @JoinColumn(name = "group_id", nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "event_id", nullable = false, updatable = false))
    private Set<Event> events = new HashSet<>();


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "deadline_group",
            joinColumns = @JoinColumn(name = "group_id", nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "deadline_id", nullable = false, updatable = false))
    private Set<Deadline> deadlines = new HashSet<>();

    public Group() {
    }

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(
            name = "user_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "group_user_id_fk"
            )
    )
    private User group_user;

    public Group(String group_name) {
        this.group_name = group_name;
    }

    public Group(String group_name, String group_description) {
        this.group_name = group_name;
        this.group_description = group_description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_description() {
        return group_description;
    }

    public void setGroup_description(String group_description) {
        this.group_description = group_description;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> evens) {
        this.events = events;
    }

    public Set<Deadline> getDeadlines() {
        return deadlines;
    }

    public void setDeadlines(Set<Deadline> deadlines) {
        this.deadlines = deadlines;
    }

    public User getUser() {
        return group_user;
    }

    public void setUser(User group_user) {
        this.group_user = group_user;
    }


    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", group_name='" + group_name + '\'' +
                ", group_description='" + group_description + '\'' +
                '}';
    }

    // TODO: Check after deadline PUT tests

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(id, group.id) && Objects.equals(group_name, group.group_name) && Objects.equals(group_description, group.group_description) && Objects.equals(events, group.events) && Objects.equals(deadlines, group.deadlines) && Objects.equals(group_user, group.group_user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, group_name, group_description, events, deadlines, group_user);
    }
}
