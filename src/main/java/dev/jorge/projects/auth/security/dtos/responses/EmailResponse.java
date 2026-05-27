package dev.jorge.projects.auth.security.dtos.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class EmailResponse {

    private UUID userId;
    private String emailTo;
    private String subject;
    private String text;
}