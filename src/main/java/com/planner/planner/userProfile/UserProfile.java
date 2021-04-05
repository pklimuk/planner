package com.planner.planner.userProfile;

import com.planner.planner.user.User;

import javax.persistence.*;
import java.time.LocalDate;

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

    @OneToOne(
            mappedBy = "user_profile",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    private User user;
//    @OneToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "user_id", nullable = false)
//
//    private User user;

    public UserProfile(String firstName, String lastName, String email, LocalDate dob) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.dob = dob;
    }

    public UserProfile() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", dob=" + dob +
                '}';
    }
}
