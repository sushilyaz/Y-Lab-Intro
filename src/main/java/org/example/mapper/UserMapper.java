package org.example.mapper;

import org.example.dto.UserCreateDTO;
import org.example.dto.UserDTO;
import org.example.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * маппер для User
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
componentModel = "spring")
public interface UserMapper {

    User map(UserCreateDTO userCreateDTO);
    UserDTO map(User user);
}


