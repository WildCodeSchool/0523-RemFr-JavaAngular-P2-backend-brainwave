package com.templateproject.api.service;

public interface EmailSenderService {

    public void sendEmail(
            String name,
            String fromAddress,
            String toAddress,
            String body) throws  Exception;
}
