package org.example.mapper;

import org.example.dto.UserCreateDTO;
import org.example.dto.UserDTO;
import org.example.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

/**
 * маппер для User
 */
@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    User map(UserCreateDTO userCreateDTO);
    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    UserDTO map(User user);
}


