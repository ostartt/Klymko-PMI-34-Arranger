package com.arranger.eurekaclient.controller;

import com.softserve.edu.sporthubujp.dto.UserDTO;
import com.softserve.edu.sporthubujp.dto.UserSavePasswordDTO;
import com.softserve.edu.sporthubujp.dto.UserSaveProfileDTO;
import com.softserve.edu.sporthubujp.entity.User;
import com.softserve.edu.sporthubujp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import net.snowflake.client.jdbc.internal.google.protobuf.ServiceException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping(path = "/profile")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<UserDTO> updateProfile(@NotNull Principal principal,
        @RequestBody @Valid UserSaveProfileDTO newUser) {
        User user = userService.findUserByEmail(principal.getName());
        log.info(String.format("Controller: updating user with id %s", user.getId()));
        return ResponseEntity.status(HttpStatus.OK).body(
            userService.updateUser(user, newUser));
    }

    @PutMapping(path = "/password")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<UserDTO> updatePassword(@NotNull Principal principal,
        @RequestBody @Valid UserSavePasswordDTO newPassword) throws ServiceException {
        User user = userService.findUserByEmail(principal.getName());
        log.info(String.format("Controller: updating password with id %s", user.getId()));
        return ResponseEntity.status(HttpStatus.OK).body(
            userService.updatePassword(user, newPassword));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserSaveProfileDTO> getUserById(@PathVariable String id) {
        log.info("Get user by id {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(
            userService.findUserById(id));
    }

    @GetMapping(path = "/profile")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<UserDTO> getUser(@NotNull Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        log.info(String.format("Controller: get user with id %s", user.getId()));
        return ResponseEntity.status(HttpStatus.OK).body(
            userService.getUser(user));
    }
}