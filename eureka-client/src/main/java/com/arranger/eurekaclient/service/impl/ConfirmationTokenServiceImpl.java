package com.arranger.eurekaclient.service.impl;

import com.arranger.eurekaclient.entity.ConfirmationToken;
import com.arranger.eurekaclient.repository.ConfirmationTokenRepository;
import com.arranger.eurekaclient.service.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Override
    public void saveConfirmationToken(ConfirmationToken token) {
        log.info("Saving token {}", token.getToken());
        confirmationTokenRepository.save(token);
    }

    @Override
    public Optional<ConfirmationToken> getToken(String token) {
        log.debug("Getting token {}", token);
        return confirmationTokenRepository.findByToken(token);
    }

    @Override
    public void setConfirmedAt(String token) {
        log.debug("Confirming token {}", token);
        confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
