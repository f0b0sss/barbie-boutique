package com.barbieboutique.user.controller;


import com.barbieboutique.exceptions.PasswordsNotEqualsException;
import com.barbieboutique.exceptions.UserAlreadyExistException;
import com.barbieboutique.user.dto.UserDTO;
import com.barbieboutique.user.entity.User;
import com.barbieboutique.user.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;
import java.util.ResourceBundle;

@Controller
@AllArgsConstructor
public class AuthorizationController {
    private final UserService userService;

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
    public String registration(@Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signin";
        }

        try {
            userService.save(userDTO);
        } catch (UserAlreadyExistException e) {
            bindingResult.rejectValue("email", "email.exist",
                    "Email already exists");
            return "signin";
        } catch (PasswordsNotEqualsException e) {
            bindingResult.rejectValue("matchingPassword", "password.matchingPassword",
                    "Passwords don't match");
            return "signin";
        }

        return "redirect:/activation";
    }

    @GetMapping("/activation")
    public String activationPage() {
        return "activation";
    }


}
