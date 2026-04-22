package dev.jorge.projects.auth.api.common.dto.response;

import java.util.Date;

public record ExceptionResponse(String message, String details, Date timeStamp) {}
