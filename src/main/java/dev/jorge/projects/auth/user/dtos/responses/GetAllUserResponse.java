package dev.jorge.projects.auth.user.dtos.responses;

import org.springframework.data.domain.Page;

import java.util.List;

public record GetAllUserResponse(
        List<GetOneUserResponse> content,
        int page,
        int pageSize,
        long totalElements,
        int totalPages
)
{
    public static GetAllUserResponse fromPage(Page<GetOneUserResponse> page) {
        return new GetAllUserResponse(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}