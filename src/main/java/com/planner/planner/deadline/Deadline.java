package com.planner.planner.deadline;

import com.planner.planner.group.Group;
import com.planner.planner.user.User;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "Users_Deadlines")
@Table(
        name = "users_deadlines"
)
public class Deadline {

    @Id
    @SequenceGenerator(
            name = "deadline_sequence",
            sequenceName = "deadline_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "deadline_sequence"
    )

    @Column(
            name = "deadline_id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "deadline_title",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String title;

    @Column(
            name = "deadline_time",
            nullable = false,
            columnDefinition = "TIMESTAMP WITHOUT TIME ZONE"
    )
    private LocalDateTime deadline_time;

    @Column(
            name = "deadline_description",
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
                    name = "deadline_user_id_fk"
            )
    )
    private User deadline_user;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToMany(mappedBy = "deadlines", fetch = FetchType.LAZY)
    private Set<Group> groups = new HashSet<>();


    public Deadline() {
    }

    public Deadline(String title, LocalDateTime deadline_time, String description) {
        this.title = title;
        this.deadline_time = deadline_time;
        this.description = description;
    }

    public Deadline(String title, LocalDateTime deadline_time, String description, Set<Group> groups) {
        this.title = title;
        this.deadline_time = deadline_time;
        this.description = description;
        this.groups = groups;
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

    public LocalDateTime getDeadline_time() {
        return deadline_time;
    }

    public void setDeadline_time(LocalDateTime deadline_time) {
        this.deadline_time = deadline_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return deadline_user;
    }

    public void setUser(User deadline_user) {
        this.deadline_user = deadline_user;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "Deadline{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", deadline_time=" + deadline_time +
                ", description='" + description + '\'' +
                ", deadline_user=" + deadline_user +
                '}';
    }
}