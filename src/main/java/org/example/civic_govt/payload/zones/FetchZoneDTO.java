package org.example.civic_govt.payload.zones;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FetchZoneDTO {
    private Long id;
    private String name;
    private String districtName;
    private String zoneHeadName;
    private Long numberOfZoneOfficials;
    private Long numberOfIssues;
}
