package com.arranger.eurekaclient.service.impl;

import com.arranger.eurekaclient.dto.UserDTO;
import com.arranger.eurekaclient.entity.ConfirmationToken;
import com.arranger.eurekaclient.entity.User;
import com.arranger.eurekaclient.mapper.UserMapper;
import com.arranger.eurekaclient.repository.UserRepository;
import com.arranger.eurekaclient.security.PasswordConfig;
import com.arranger.eurekaclient.service.ConfirmationTokenService;
import com.arranger.eurekaclient.service.EmailSenderService;
import com.arranger.eurekaclient.service.UserService;
import com.arranger.eurekaclient.validator.PasswordValidator;
import com.google.common.io.Files;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.SendFailedException;
import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final static String EMAIL_ALREADY_TAKEN = "Service: email %s already taken";
    private final UserRepository userRepository;
    private final PasswordConfig passwordConfig;
    private final UserMapper userMapper;

    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public String signUpUser(UserDTO userDTO) {

        log.info(String.format("Service: signing up user with the email %s", userDTO.getEmail()));

        User user = userMapper.dtoToEntity(userDTO);
        boolean userExists = userRepository
            .findByEmail(userDTO.getEmail())
            .isPresent();

        if (userExists) {
            log.error(String.format(EMAIL_ALREADY_TAKEN, userDTO.getEmail()));
            throw new IllegalArgumentException(EMAIL_ALREADY_TAKEN);
        }

        String encodedPassword = passwordConfig.passwordEncoder()
            .encode(userDTO.getPassword());

        user.setPassword(encodedPassword);
        user.setCreateDateTime(LocalDateTime.now());

        log.info(String.format("Service: saving user with the email %s", userDTO.getEmail()));
        userRepository.save(user);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
            token,
            LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(15),
            user
        );

        confirmationTokenService.saveConfirmationToken(
            confirmationToken);

        return token;
    }

    @Override
    public void enableUser(String email) {
        log.debug(String.format("enabling user with the email %s", email));
        userRepository.enableUser(email);
    }

    @Override
    public User findUserByEmail(String email) {
        log.info(String.format("find user with the email %s", email));
        return userRepository
                .findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
    }
}