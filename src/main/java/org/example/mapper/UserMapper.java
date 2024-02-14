package org.example.mapper;

import org.example.dto.UserCreateDTO;
import org.example.dto.UserDTO;
import org.example.model.Role;
import org.example.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * маппер для User
 */
@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    User map(UserCreateDTO userCreateDTO);


    UserDTO map(User user);
    default Role mapRoleFromString(String role) {
        return Role.valueOf(role);
    }

    default String mapRoleToString(Role role) {
        return (role != null) ? role.name() : null;
    }
}


