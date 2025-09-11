package org.example.civic_govt.payload.districts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDistrictDTO {
    private String name;
    private String departmentName;
}
