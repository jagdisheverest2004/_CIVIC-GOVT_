package org.example.civic_govt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(unique = true)
    private String email;

    @ManyToOne
    @JoinColumn(name = "department_id")
    @JsonIgnore
    private Department department; // Relevant if user is an OFFICIAL

    @OneToMany(mappedBy = "reporter")
    @JsonIgnore
    private List<Issue> reportedIssues; // Corrected mapping for issues reported by the user

    @OneToMany(mappedBy = "assignee")
    @JsonIgnore
    private List<Issue> assignedIssues; // New mapping for issues assigned to the user

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Vote> votes;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Comment> comments;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Notification> notifications;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime createdAt = LocalDateTime.now();

    public User(String username, String email, String passwordHash, Role role){
        this.username = username;
        this.email = email;
        this.password = passwordHash;
        this.role = role;
    }

    public enum Role {
        HEAD,
        ADMIN,
        CITIZEN,
        OFFICIAL
    }
}