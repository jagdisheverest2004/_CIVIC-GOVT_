package org.example.civic_govt.payload.accounts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountDTO {
    private String username;
    private String password;
    private String email;
    private String zoneName; // Required for zone_official
    private String districtName; // Required for zone_official and dist_head
    private String departmentName; // Required for dist_head
}
