package org.example.civic_govt.payload.zones;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateZoneDTO {
    private String name;
    private String districtName;
}
