package com.planner.planner.deadline;

import com.planner.planner.user.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

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
            name = "id",
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

    @Column(
            name = "deadline_group",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String group;

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


    public Deadline() {
    }

    public Deadline(String title, LocalDateTime deadline_time, String description, String group) {
        this.title = title;
        this.deadline_time = deadline_time;
        this.description = description;
        this.group = group;
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public User getUser() {
        return deadline_user;
    }

    public void setUser(User deadline_user) {
        this.deadline_user = deadline_user;
    }

    @Override
    public String toString() {
        return "Deadline{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", deadline_time=" + deadline_time +
                ", description='" + description + '\'' +
                ", group='" + group + '\'' +
                ", user=" + deadline_user +
                '}';
    }
}