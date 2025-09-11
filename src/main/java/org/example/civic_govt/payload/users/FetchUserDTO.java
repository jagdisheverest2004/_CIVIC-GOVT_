package org.example.civic_govt.payload.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FetchUserDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String department;
    private String district;
    private String zone;
}
