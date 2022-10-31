package com.arranger.eurekaclient.service;

import com.arranger.eurekaclient.dto.RegistrationRequestDTO;

import javax.mail.SendFailedException;
import java.io.IOException;

public interface RegistrationService {

    public String register(RegistrationRequestDTO request) throws IOException, SendFailedException;
    public String confirmToken(String token);
}
