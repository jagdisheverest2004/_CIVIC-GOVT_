package org.example.civic_govt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JoinColumn(name = "dept_head_id")
    private User deptHead;

    @OneToMany(mappedBy = "department")
    private List<District> districts;

    @OneToMany(mappedBy = "department")
    private List<Issue> issues;

    @OneToMany(mappedBy = "department")
    private List<User> districtOfficials;

    @ElementCollection
    @CollectionTable(name = "department_issue_types", joinColumns = @JoinColumn(name = "department_id"))
    @Column(name = "issue_type")
    private List<String> defaultIssueTypes;

}