package com.arranger.eurekaclient.service;

import com.arranger.eurekaclient.entity.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokenService {

    public void saveConfirmationToken(ConfirmationToken token);

    public Optional<ConfirmationToken> getToken(String token);

    public void setConfirmedAt(String token);
}
