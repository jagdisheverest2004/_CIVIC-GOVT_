package org.example.civic_govt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "departments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(name = "head_user_id")
    private User head;

    @OneToMany(mappedBy = "department")
    private List<Issue> issues;

    // Added a list of officials to this department
    @OneToMany(mappedBy = "department")
    private List<User> officials;
}