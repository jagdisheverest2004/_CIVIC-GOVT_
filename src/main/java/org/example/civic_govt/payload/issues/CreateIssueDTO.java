package org.example.civic_govt.payload.issues;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.civic_govt.model.Issue;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateIssueDTO {
    private String title;
    private Double latitude;
    private Double longitude;
    private String departmentName;
    private String districtName;
    private String zoneName;
    private Issue.Priority priority;
    private MultipartFile[] images;
}
