package com.arranger.eurekaclient.dto;

import com.arranger.eurekaclient.entity.Role;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AuthenticationResponseDTO {
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private String jwt;
}