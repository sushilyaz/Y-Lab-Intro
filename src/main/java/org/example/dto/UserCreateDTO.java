package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserCreateDTO {
    @NotBlank
    @Size(min = 4)
    private String username;
    @NotBlank
    @Size(min = 4)
    private String password;

//    @JsonCreator
//    public UserCreateDTO(@JsonProperty("username") String username, @JsonProperty("password") String password){
//        this.username = username;
//        this.password = password;
//    }

}
