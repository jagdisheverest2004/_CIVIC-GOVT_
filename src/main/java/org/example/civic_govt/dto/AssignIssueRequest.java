package org.example.civic_govt.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class AssignIssueRequest {
    @NotNull
    private Long assigneeId;
}

