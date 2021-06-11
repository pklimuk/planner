package com.planner.planner.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
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

    public User getUser() {
        return group_user;
    }

    public void setUser(User group_user) {
        this.group_user = group_user;
    }
}
