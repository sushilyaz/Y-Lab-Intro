package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO для вывода всей информации о всех пользователях (для админа)
 * Не включил поле password, хотя для администратора можно было бы и добавить (наверное)
 */

@AllArgsConstructor
@Getter
@Setter
public class UserInfoDTO {
    private String username;
    private LocalDate date;
    private String type;
    private Double value;
}
