package org.example.civic_govt.payload.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FetchCitizenDTO {
    private Long id;
    private String username;
    private String email;
    private String role;
    private List<String> reportedIssues;
    private Long commentedCounts;
    private Long upvotedCounts;
}
