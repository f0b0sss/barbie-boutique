package com.barbieboutique.registrationAPI.controller;


import com.barbieboutique.exceptions.PasswordsNotEqualsException;
import com.barbieboutique.exceptions.UserAlreadyExistException;
import com.barbieboutique.registrationAPI.entity.OnRegistrationCompleteEvent;
import com.barbieboutique.registrationAPI.entity.VerificationToken;
import com.barbieboutique.registrationAPI.service.VerificationService;
import com.barbieboutique.user.dto.UserDTO;
import com.barbieboutique.user.entity.Role;
import com.barbieboutique.user.entity.Status;
import com.barbieboutique.user.entity.User;
import com.barbieboutique.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import java.util.Calendar;
import java.util.Locale;

@Controller
@AllArgsConstructor
public class AuthorizationController {
    private final VerificationService verificationService;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;
    private final MessageSource messages;

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

    @PostMapping("/registration")
    public String registration(@Valid UserDTO userDTO, HttpServletRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signin";
        }

        try {
            User registered = userService.save(userDTO);

            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl));
        } catch (UserAlreadyExistException e) {
            bindingResult.rejectValue("email", "email.exist",
                    "Email already exists");
            return "signin";
        } catch (PasswordsNotEqualsException e) {
            bindingResult.rejectValue("matchingPassword", "password.matchingPassword",
                    "Passwords don't match");
            return "signin";
        }

        return "redirect:/registration";
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {
        Locale locale = request.getLocale();

        VerificationToken verificationToken = verificationService.getVerificationToken(token);
        if (verificationToken == null) {
            String message = messages.getMessage("auth.message.invalidToken", null, locale);

            model.addAttribute("message", message);

            return "redirect:/badUser?lang=" + locale.getLanguage();
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            String messageValue = messages.getMessage("auth.message.expired", null, locale);

            model.addAttribute("message", messageValue);

            return "redirect:/badUser?lang=" + locale.getLanguage();
        }

        user.setEnabled(true);
        user.setStatus(Status.ACTIVE);
        user.setRole(Role.USER);
        userService.save(user);

        return "redirect:/login?lang=" + request.getLocale().getLanguage();
    }

    @GetMapping("/registration")
    public String activationPage() {
        return "registration";
    }

    @GetMapping("/badUser")
    public String badUser() {
        return "badUser";
    }


}
