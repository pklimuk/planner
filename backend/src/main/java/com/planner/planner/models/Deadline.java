package com.planner.planner.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
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

    public Deadline(String title, LocalDateTime deadline_time, String description) {
        this.title = title;
        this.deadline_time = deadline_time;
        this.description = description;
    }

    public User getUser() {
        return deadline_user;
    }

    public void setUser(User deadline_user) {
        this.deadline_user = deadline_user;
    }
}
