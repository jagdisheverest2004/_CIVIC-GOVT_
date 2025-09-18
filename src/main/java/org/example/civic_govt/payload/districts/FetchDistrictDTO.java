package org.example.civic_govt.payload.districts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FetchDistrictDTO {
    private Long id;
    private String name;
    private String departmentName;
    private String districtHeadName;
    private Long numberOfZones;
    private Long numberOfZoneOfficials;
    private Long numberOfIssues;
}
