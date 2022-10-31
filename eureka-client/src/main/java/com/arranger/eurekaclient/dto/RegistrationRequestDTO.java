package com.arranger.eurekaclient.dto;

import com.arranger.eurekaclient.validator.EmailConstraint;
import com.arranger.eurekaclient.validator.NameConstraint;
import com.arranger.eurekaclient.validator.PasswordConstraint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequestDTO {
    @NotNull
    @NameConstraint
    private final String firstName;

    @NotNull
    @NameConstraint
    private final String lastName;

    @NotNull
    @EmailConstraint
    private final String email;

    @NotNull
    @PasswordConstraint
    private final String password;
}