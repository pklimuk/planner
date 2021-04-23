package com.planner.planner.group;

import com.planner.planner.deadline.Deadline;
import com.planner.planner.event.Event;

import javax.persistence.*;
import java.util.HashSet;
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

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", group_name='" + group_name + '\'' +
                ", group_description='" + group_description + '\'' +
                '}';
    }
}
