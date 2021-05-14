package com.planner.planner.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Users_Events")
@Table(
        name = "users_events"
)
public class Event {

    @Id
    @SequenceGenerator(
            name = "event_sequence",
            sequenceName = "event_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "event_sequence"
    )

    @Column(
            name = "event_id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "event_title",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String title;

    @Column(
            name = "event_start_time",
            nullable = false,
            columnDefinition = "TIMESTAMP WITHOUT TIME ZONE"
    )
    private LocalDateTime start;

    @Column(
            name = "event_end_time",
            nullable = false,
            columnDefinition = "TIMESTAMP WITHOUT TIME ZONE"
    )
    private LocalDateTime end;

    @Column(
            name = "event_description",
            columnDefinition = "TEXT"
    )
    private String description;


    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(
            name = "user_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "event_user_id_fk"
            )
    )
    private User event_user;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToMany(mappedBy = "events", fetch = FetchType.LAZY)
    private Set<Group> groups = new HashSet<>();

    public Event() {
    }

    public Event(String title, LocalDateTime start, LocalDateTime end, String description) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return event_user;
    }

    public void setUser(User event_user) {
        this.event_user = event_user;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", description='" + description + '\'' +
                ", event_user=" + event_user +
                '}';
    }
}
