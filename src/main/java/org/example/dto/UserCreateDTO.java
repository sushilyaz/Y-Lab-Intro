package org.example.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserCreateDTO {
    @NotBlank
    @Size(min = 4)
    private String username;
    @NotBlank
    @Size(min = 4)
    private String password;

    @JsonCreator
    public UserCreateDTO(@JsonProperty("username") String username, @JsonProperty("password") String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
