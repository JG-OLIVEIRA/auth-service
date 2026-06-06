package dev.jorge.projects.auth.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailResponse {
    private String userId;
    private String emailTo;
    private String subject;
    private String text;
}