package org.example.civic_govt.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class CommentRequest {
    @NotBlank
    private String text;
}

