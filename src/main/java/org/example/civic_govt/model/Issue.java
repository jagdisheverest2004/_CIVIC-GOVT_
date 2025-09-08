package org.example.civic_govt.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "issues")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String category;
    private Double locationLat;
    private Double locationLong;
    private String priority;
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne
    @JoinColumn(name = "reporter_id")
    private User reporter;
    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;
    private String mediaUrl;
    private LocalDateTime createdAt;

    public enum Status {
        PENDING, IN_PROGRESS, RESOLVED
    }
}

