package com.barbieboutique.registrationAPI.controller;


import com.barbieboutique.exceptions.PasswordsNotEqualsException;
import com.barbieboutique.exceptions.UserAlreadyExistException;
import com.barbieboutique.exceptions.UserNotFoundException;
import com.barbieboutique.registrationAPI.entity.OnRegistrationCompleteEvent;
import com.barbieboutique.registrationAPI.entity.Token;
import com.barbieboutique.registrationAPI.service.AuthorizationService;
import com.barbieboutique.registrationAPI.service.TokenService;
import com.barbieboutique.site.entity.Notification;
import com.barbieboutique.user.dto.PasswordDto;
import com.barbieboutique.user.dto.UserDTO;
import com.barbieboutique.user.entity.Role;
import com.barbieboutique.user.entity.Status;
import com.barbieboutique.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class AuthorizationController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private MessageSource messages;
    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    @GetMapping("/signin")
    public String signIn(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "signin";
    }

    @Transactional
    @PostMapping("/registration")
    public String registration(HttpServletRequest request, @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signin";
        }
        try {
            User registeredUser = authorizationService.save(userDTO);
            Locale locale = LocaleContextHolder.getLocale();

            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registeredUser, locale, appUrl));
        } catch (UserAlreadyExistException e) {
            bindingResult.rejectValue("email", "email.exist",
                    "Email already exists");
            return "signin";
        } catch (PasswordsNotEqualsException e) {
            bindingResult.rejectValue("matchingPassword", "password.matchingPassword",
                    "Passwords don't match");
            return "signin";
        }

        return "redirect:/registration-success";
    }

    @GetMapping("/registration-success")
    public String registrationNotification(Model model) {
        Locale locale = LocaleContextHolder.getLocale();

        String title = messages.getMessage("auth.message.regSuccess", null, locale);
        String header = messages.getMessage("auth.message.regSuccess", null, locale);
        String message = messages.getMessage("auth.message.sendToken", null, locale);

        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setHeader(header);
        notification.setMessage(message);

        model.addAttribute("notification", notification);
        return "notification";
    }

    @Transactional
    @GetMapping("/registrationConfirm")
    public String registrationConfirm(Model model, @RequestParam String token) {
        Locale locale = LocaleContextHolder.getLocale();

        Token verificationToken = tokenService.getToken(token);
        if (verificationToken == null) {
            String message = messages.getMessage("auth.message.invalidToken", null, locale);

            model.addAttribute("message", message);

            return "redirect:/bad-user?lang=" + locale.getLanguage();
        }

        User user = verificationToken.getUser();

        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            String message = messages.getMessage("auth.message.expired", null, locale);

            model.addAttribute("message", message);

            return "redirect:/token-error?errorCode=" + "auth.message.expired" +
                    "&lang=" + locale.getLanguage() +
                    "&token=" + token;
        }

        user.setEnabled(true);
        user.setStatus(Status.ACTIVE);
        user.setRole(Role.USER);
        authorizationService.save(user);

        return "redirect:/login?lang=" + locale.getLanguage();
    }

    @GetMapping("/bad-user")
    public String badUserNotification(Model model) {
        Locale locale = LocaleContextHolder.getLocale();

        String title = messages.getMessage("badUser.title", null, locale);
        String message = messages.getMessage("badUser.message", null, locale);

        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(message);

        model.addAttribute("notification", notification);
        return "notification";
    }

    @GetMapping("/token-error")
    public String updatePasswordError(Model model, @RequestParam String errorCode, @RequestParam String token) {
        String message = messages.getMessage(errorCode, null, LocaleContextHolder.getLocale());
        model.addAttribute("message", message);
        model.addAttribute("errorCode", errorCode);
        model.addAttribute("token", token);

        return "login";
    }

    @GetMapping("/reset-password")
    public String resetPassword() {
        return "reset-password";
    }

    @Transactional
    @PostMapping("/update-password")
    public String reset(HttpServletRequest request, @RequestParam String email) throws UserNotFoundException {
        User user = authorizationService.findByEmail(email);

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        String token = UUID.randomUUID().toString();

        authorizationService.createPasswordResetTokenForUser(user, token);

        String url = request.getRequestURL().toString();

        String message = messages.getMessage("messageEmail.resetPasswordEmail", null, LocaleContextHolder.getLocale());
        String subject = messages.getMessage("message.resetPassword", null, LocaleContextHolder.getLocale());

        mailSender.send(constructResetTokenEmail(subject, message, url, token, user));

        return "redirect:/sent-password";
    }

    private SimpleMailMessage constructResetTokenEmail(String subject, String message, String url,
                                                       String token, User user) {
        String link = url + "?token=" + token + "&lang=" + LocaleContextHolder.getLocale();

        return constructEmail(subject, message + " \r\n" + link, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body, User user) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(System.getProperty("mail.username"));

        return email;
    }

    @GetMapping("/sent-password")
    public String sentPasswordNotification(Model model) {
        Locale locale = LocaleContextHolder.getLocale();

        String title = messages.getMessage("auth.passwordUpdate.title", null, locale);
        String message = messages.getMessage("message.sentLink", null, locale);

        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(message);

        model.addAttribute("notification", notification);
        return "notification";
    }

    @GetMapping("/update-password")
    public String updatePasswordForm(Model model, @RequestParam String token) {
        String result = tokenService.validateToken(token);

        Locale locale = LocaleContextHolder.getLocale();

        if (result != null) {
            return "redirect:/update-password-error?errorCode=" + "auth.message." + result +
                    "&lang=" + locale.getLanguage() +
                    "&token=" + token;
        } else {

            PasswordDto passwordDto = new PasswordDto();
            passwordDto.setToken(token);

            model.addAttribute("passwordDto", passwordDto);

            return "update-password";
        }
    }

    @Transactional
    @PostMapping("/updatePassword")
    public String updating(@Valid PasswordDto passwordDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "update-password";
        }

        String result = tokenService.validateToken(passwordDto.getToken());
        Locale locale = LocaleContextHolder.getLocale();

        if (result != null) {
            return "redirect:/token-error?errorCode=" + "auth.message." + result +
                    "&lang=" + locale.getLanguage() +
                    "&token=" + passwordDto.getToken();
        } else {
            Optional user = tokenService.getUserByToken(passwordDto.getToken());

            if (user.isPresent()) {
                try {
                    authorizationService.changeUserPassword((User) user.get(), passwordDto);
                } catch (PasswordsNotEqualsException e) {
                    bindingResult.rejectValue("matchingPassword", "password.matchingPassword",
                            "Passwords don't match");
                    return "update-password";
                }

                return "redirect:/update-password-success";
            } else {
                return "redirect:/bad-user";
            }
        }
    }

    @GetMapping("/update-password-success")
    public String updatePasswordSuccessNotification(Model model) {
        Locale locale = LocaleContextHolder.getLocale();

        String title = messages.getMessage("auth.passwordUpdate.title", null, locale);
        String header = messages.getMessage("auth.passwordUpdated.success", null, locale);

        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(header);

        model.addAttribute("notification", notification);
        return "notification";
    }

    @Transactional
    @PostMapping("/resendToken/{token}")
    public String resendToken(HttpServletRequest request, Model model, @PathVariable String token) {
        Locale locale = LocaleContextHolder.getLocale();
        Token verificationToken = tokenService.getToken(token);

        if (verificationToken == null) {
            String message = messages.getMessage("auth.message.invalidToken", null, locale);

            model.addAttribute("message", message);

            return "redirect:/bad-user?lang=" + locale.getLanguage();
        } else {
            User user = verificationToken.getUser();

            String newToken = UUID.randomUUID().toString();

            authorizationService.createPasswordResetTokenForUser(user, newToken);

            if (user.isEnabled()) {
                String message = messages.getMessage("messageEmail.resetPasswordEmail", null, locale);
                String subject = messages.getMessage("message.resetPassword", null, locale);

                String url = request.getRequestURL().toString()
                        .replace("resendToken/" + token, "update-password");

                mailSender.send(constructResetTokenEmail(subject, message, url, newToken, user));

                return "redirect:/sent-password";
            } else {
                String message = messages.getMessage("auth.message.emailTitleConfirmRegister", null, locale);
                String subject = messages.getMessage("auth.message.regSuccess", null, locale);

                String url = request.getRequestURL().toString()
                        .replace("resendToken/" + token, "registrationConfirm");

                mailSender.send(constructResetTokenEmail(subject, message, url, newToken, user));

                return "redirect:/registration";
            }
        }
    }

}
