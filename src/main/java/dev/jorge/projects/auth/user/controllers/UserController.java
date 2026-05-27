package dev.jorge.projects.auth.user.controllers;

import dev.jorge.projects.auth.user.dtos.responses.GetAllUserResponse;
import dev.jorge.projects.auth.user.dtos.responses.GetOneUserResponse;
import dev.jorge.projects.auth.user.entities.User;
import dev.jorge.projects.auth.user.services.UserService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.hateoas.EntityModel;

import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping("/api/v1/users/{userId}")
    public ResponseEntity<EntityModel<GetOneUserResponse>> getOneUser(@PathVariable UUID userId) {
        User user = userService.findById(userId);

        return ResponseEntity.ok(
                EntityModel.of(
                        GetOneUserResponse.fromEntity(user),
                        linkTo(methodOn(UserController.class).getOneUser(userId)).withSelfRel(),
                        linkTo(methodOn(UserController.class).getAllUsers(0, 10)).withRel("users")
                )
        );
    }

    @GetMapping("/api/v1/users")
    public ResponseEntity<EntityModel<GetAllUserResponse>> getAllUsers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize
    )
    {
        Page<GetOneUserResponse> users = userService.findAll(PageRequest.of(page, pageSize))
                .map(GetOneUserResponse::fromEntity);

        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(UserController.class).getAllUsers(page, pageSize)).withSelfRel());

        if (users.hasNext())
            links.add(linkTo(methodOn(UserController.class).getAllUsers(page + 1, pageSize)).withRel("next"));

        if (users.hasPrevious())
            links.add(linkTo(methodOn(UserController.class).getAllUsers(page - 1, pageSize)).withRel("prev"));

        return ResponseEntity.ok(EntityModel.of(GetAllUserResponse.fromPage(users), links));
    }

}
