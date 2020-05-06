package com.group14.termproject.client.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String username;
    private String password;

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

