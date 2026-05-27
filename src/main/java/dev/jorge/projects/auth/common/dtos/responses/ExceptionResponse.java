package dev.jorge.projects.auth.common.dtos.responses;

import java.util.Date;

public record ExceptionResponse(String message, String details, Date timeStamp) {}
