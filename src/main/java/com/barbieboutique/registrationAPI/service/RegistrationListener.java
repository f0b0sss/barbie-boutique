package com.barbieboutique.registrationAPI.service;

import com.barbieboutique.registrationAPI.entity.OnRegistrationCompleteEvent;
import com.barbieboutique.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.UUID;

@AllArgsConstructor
@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    private final TokenService tokenService;
    private final MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        Locale locale = LocaleContextHolder.getLocale();
        User user = event.getUser();
        String token = UUID.randomUUID().toString();

        tokenService.createToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = messages.getMessage(
                "auth.message.emailTitleConfirmRegister", null, locale);
        String confirmationUrl = "/registrationConfirm?token=" + token;
        String message = messages.getMessage(
                "auth.message.regSuccess", null, locale);

        String appUrl = messages.getMessage("appUrl", null, locale);

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + appUrl + confirmationUrl);

        mailSender.send(email);
    }
}