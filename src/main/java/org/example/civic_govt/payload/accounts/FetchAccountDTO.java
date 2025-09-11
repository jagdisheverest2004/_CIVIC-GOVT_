package org.example.civic_govt.payload.accounts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FetchAccountDTO {
    private Long id;
    private String username;
    private String email;
    private String role;
}
