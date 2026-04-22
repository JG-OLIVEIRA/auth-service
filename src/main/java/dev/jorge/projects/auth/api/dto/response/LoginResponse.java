package dev.jorge.projects.auth.api.dto.response;

public record LoginResponse (String token)
{
    public static  LoginResponse fromEntity(String token)
    {
        return new LoginResponse(token);
    }
}
