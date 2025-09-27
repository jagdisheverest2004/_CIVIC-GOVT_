package org.example.civic_govt.payload.departments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FetchDepartmentDTO {
    private Long id;
    private String name;
    private String deptHeadName;
    private Integer numberOfDistricts;
    private Integer numberOfIssues;
    private Integer numberOfOfficials;
    private List<String> defaultIssueTypes;
}
