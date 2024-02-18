package org.example.utils;

import org.example.dto.UserCreateDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Реализация валидации на регистрацию пользователя
 */
@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return UserCreateDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserCreateDTO createDTO = (UserCreateDTO) target;
        if (createDTO.getUsername().length() < 4 || createDTO.getUsername().length() > 12) {
            errors.rejectValue("username", "", "Username not valid, size of username must be min 4, max 12");
        }
        if (createDTO.getPassword().length() < 4 || createDTO.getPassword().length() > 12) {
            errors.rejectValue("password", "", "Password not valid, size of username must be min 4, max 12");
        }
    }
}
