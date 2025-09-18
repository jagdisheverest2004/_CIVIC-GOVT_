package org.example.civic_govt.payload.issues;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FetchIssueDTO {
    private Long id;
    private String title;
    private String status;
    private String priority;
    private String departmentName;
    private String districtName;
    private String zoneName;
    private Double latitude;
    private Double longitude;
    private List<String> reporters;
    private List<String> photosUrls;
    private Long upvoteCount;
    private Long commentCount;
    private List<String> photos;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
