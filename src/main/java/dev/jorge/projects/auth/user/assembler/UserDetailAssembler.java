package dev.jorge.projects.auth.user.assembler;

import dev.jorge.projects.auth.user.controller.UserController;
import dev.jorge.projects.auth.user.dto.response.GetUserResponse;
import dev.jorge.projects.auth.user.entity.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserDetailAssembler {

    public EntityModel<GetUserResponse> toModel(User user) {
        GetUserResponse dto = new GetUserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRoles(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );

        return EntityModel.of(dto,
                linkTo(methodOn(UserController.class)
                        .getOneUser(user.getId()))
                        .withSelfRel(),

                linkTo(methodOn(UserController.class)
                        .getAllUsers(0, 10))
                        .withRel("users"));
    }
}