package com.barbieboutique.user.controller;


import com.barbieboutique.user.dto.UserDTO;
import com.barbieboutique.user.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
@AllArgsConstructor
public class AuthorizationController {
    private final UserService userService;

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    @GetMapping("/signin")
    public String signIn(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "signin";
    }

    @PostMapping("/signin")
    public String signInProcessing(@Valid UserDTO userDTO,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signin";
        }


        if (userService.findByEmail(userDTO.getEmail()) != null) {
            bindingResult.rejectValue("email", "user.email", "Email already exists");
            return "signin";
        }

        if (!Objects.equals(userDTO.getPassword(), userDTO.getMatchingPassword())) {
            bindingResult.rejectValue("matchingPassword", "user.matchingPassword", "Password is not equals");
            return "signin";
        }

        userService.save(userDTO);

        return "redirect:/profile";
    }
}
