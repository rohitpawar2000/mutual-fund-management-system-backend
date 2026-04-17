package com.user.user.entity.reqdto;


import lombok.Data;

@Data
public class RegisterRequestDto {
    private String email;
    private String password;

    public String getEmail() {return email; }

    public String getPassword() {
        return password;
    }

}
