package com.user.user.entity.reqdto;


import lombok.Data;

import java.util.Objects;

@Data
public class RegisterRequestDto {
    private String email;
    private String password;
    private String name;

    public String getEmail() {return email; }

    public String getPassword() {
        return password;
    }

    public String getName() {return name;}

}
