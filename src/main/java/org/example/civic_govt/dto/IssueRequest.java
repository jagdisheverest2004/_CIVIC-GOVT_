package org.example.civic_govt.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class IssueRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String category;
    @NotNull
    private Double locationLat;
    @NotNull
    private Double locationLong;
    @NotBlank
    private String priority;
    private String mediaUrl;
}

