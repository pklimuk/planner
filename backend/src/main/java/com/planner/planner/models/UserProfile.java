package com.planner.planner.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "UserProfile")
@Table(
        name = "user_profile",
        uniqueConstraints = {
                @UniqueConstraint(name = "user_profile_email_unique", columnNames = "email")
        }
)
public class UserProfile {

    @Id
    @SequenceGenerator(
            name = "user_profile_sequence",
            sequenceName = "user_profile_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_profile_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "first_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String firstName;

    @Column(
            name = "last_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String lastName;

    @Column(
            name = "email",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String email;

    @Column(
            name = "dob",
            nullable = false
    )
    private LocalDate dob;

    @Column(
            name = "profile_image",
            length = 1000
    )
    private byte[] profileImage;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne(
            mappedBy = "user_profile",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    private User user;

    public UserProfile(String firstName, String lastName, String email, LocalDate dob) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.dob = dob;
    }
}
