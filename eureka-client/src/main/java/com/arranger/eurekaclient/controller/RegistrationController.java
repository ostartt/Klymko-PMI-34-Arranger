package com.arranger.eurekaclient.controller;

import com.arranger.eurekaclient.dto.RegistrationRequestDTO;
import com.arranger.eurekaclient.service.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.SendFailedException;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping(path = "api/v1/sign-up")
@Slf4j
public class RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody @Valid RegistrationRequestDTO request)
            throws IOException, SendFailedException {
        log.info("Registering user with email {}", request.getEmail());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(registrationService.register(request));
    }

    @GetMapping(path = "confirm")
    public ResponseEntity<String> confirm(@RequestParam("token") String token) {
        log.info("Confirming token {}", token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(registrationService.confirmToken(token));
    }

}

