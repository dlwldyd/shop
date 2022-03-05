package com.example.shop.Dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class MemberFormDto {

    @NotBlank
    private String username;

    @NotBlank
    private String name;

    @NotNull
    @Email
    private String email;

    @Min(value = 8)
    private String password;

    @Min(value = 8)
    private String rePassword;

    @NotBlank
    private String address;

    @Pattern(regexp = "01[0-16-9]\\d\\d\\d\\d\\d\\d\\d\\d")
    private String phone;
}
