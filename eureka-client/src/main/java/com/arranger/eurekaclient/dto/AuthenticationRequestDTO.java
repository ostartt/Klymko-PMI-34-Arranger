package com.arranger.eurekaclient.dto;

import com.arranger.eurekaclient.validator.EmailConstraint;
import com.arranger.eurekaclient.validator.PasswordConstraint;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AuthenticationRequestDTO {
    @NotNull
    @EmailConstraint
    private String email;

    @NotNull
    @PasswordConstraint
    private String password;
}
