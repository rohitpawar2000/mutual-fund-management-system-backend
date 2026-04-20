package com.user.user.entity.reqdto;


import lombok.Data;

@Data
public class LoginDto {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
