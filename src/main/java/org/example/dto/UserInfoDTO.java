package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO для вывода всей информации о всех пользователях (для админа)
 * Не включил поле password, хотя для администратора можно было бы и добавить (наверное)
 */

@AllArgsConstructor
@Getter
@Setter
public class UserInfoDTO {
    private String username;
    private int year;
    private int month;
    private String type;
    private double value;
}
