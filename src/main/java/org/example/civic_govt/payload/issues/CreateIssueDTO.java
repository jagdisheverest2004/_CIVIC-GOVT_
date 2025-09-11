package org.example.civic_govt.payload.issues;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String priority;
}
