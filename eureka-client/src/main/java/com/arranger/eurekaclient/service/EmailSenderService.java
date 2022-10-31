package com.arranger.eurekaclient.service;

import javax.mail.SendFailedException;

public interface EmailSenderService {
    void send(String to, String email) throws SendFailedException;
}
