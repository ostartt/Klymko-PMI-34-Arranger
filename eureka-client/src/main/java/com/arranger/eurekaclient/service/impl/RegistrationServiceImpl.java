package com.arranger.eurekaclient.service.impl;

import com.arranger.eurekaclient.dto.RegistrationRequestDTO;
import com.arranger.eurekaclient.dto.UserDTO;
import com.arranger.eurekaclient.entity.ConfirmationToken;
import com.arranger.eurekaclient.entity.Role;
import com.arranger.eurekaclient.service.ConfirmationTokenService;
import com.arranger.eurekaclient.service.EmailSenderService;
import com.arranger.eurekaclient.service.RegistrationService;
import com.arranger.eurekaclient.service.UserService;
import com.arranger.eurekaclient.validator.PasswordValidator;
import com.google.common.io.Files;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.SendFailedException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

@Service
@AllArgsConstructor
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {
    private final static String TOKEN_NOT_FOUND = "Service: token %s not found";
    private final static String TOKEN_ALREADY_CONFIRMED = "Service: token %s is already confirmed";
    private final static String TOKEN_EXPIRED = "Service: token %s expired";
    private final static String INVALID_PASSWORD =
            "Service: password %s must contain at least 8 characters (letters and numbers)";

    private final static String LOGIN_ROUTE = "<meta http-equiv=\"refresh\" content=\"0;" +
            " url=http://localhost:3000/sign-in\" />";
    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenServiceImpl;
    private final EmailSenderService emailSender;
    private final PasswordValidator passwordValidator;

    @Override
    public String register(RegistrationRequestDTO request)
            throws IOException, SendFailedException {

        log.info("Registering user with email {}", request.getEmail());

        boolean isValidPassword = passwordValidator.
                test(request.getPassword());

        if (!isValidPassword) {
            log.error(String.format(INVALID_PASSWORD, request.getPassword()));
            throw new IllegalArgumentException(INVALID_PASSWORD);
        }

        String token = userService.signUpUser(
                new UserDTO(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        Role.USER)
        );

        String link = "http://localhost:8765/api/v1/sign-up/confirm?token=" + token;

        emailSender.send(
                request.getEmail(),
                buildEmail(link));

        return token;
    }

    @Transactional
    @Override
    public String confirmToken(String token) {
        log.info("Confirming token {}", token);
        ConfirmationToken confirmationToken = confirmationTokenServiceImpl
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalArgumentException(String.format(TOKEN_NOT_FOUND, token)));

        if (confirmationToken.getConfirmedAt() != null) {
            log.error(String.format(TOKEN_ALREADY_CONFIRMED, token));
            throw new IllegalArgumentException(TOKEN_ALREADY_CONFIRMED);
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            log.error(String.format(TOKEN_EXPIRED, token));
            throw new IllegalArgumentException(TOKEN_EXPIRED);
        }

        confirmationTokenServiceImpl.setConfirmedAt(token);
        userService.enableUser(
                confirmationToken.getUser().getEmail());

        return LOGIN_ROUTE;
    }

    private String buildEmail(String link) throws IOException {

        StringBuilder email = new StringBuilder(Files
                .asCharSource(new File("eureka-client/src/main/resources/template/email.html"),
                        StandardCharsets.UTF_8)
                .read());

        email
                .insert(email.indexOf("href=\"\"") + 6, link);

        return email.toString();
    }
}
