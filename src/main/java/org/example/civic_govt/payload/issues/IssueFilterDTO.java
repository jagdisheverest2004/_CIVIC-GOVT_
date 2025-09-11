package org.example.civic_govt.payload.issues;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.civic_govt.model.Issue;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueFilterDTO {
    private String departmentName;
    private String zoneName;
    private String districtName;
    private Issue.Status status;
    private Issue.Priority priority;
    private LocalDateTime updatedAfter;
}