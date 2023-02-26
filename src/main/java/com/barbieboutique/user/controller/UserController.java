package com.barbieboutique.user.controller;


import com.barbieboutique.user.dto.UserDTO;
import com.barbieboutique.user.entity.User;
import com.barbieboutique.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Objects;

@Controller
@RequestMapping("/profile")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping
//    public String userList(Model model) {
//        model.addAttribute("users", userService.getAll());
//        return "userList";
//    }

//    @PreAuthorize("hasAuthority('ADMIN')")
//    @GetMapping("/new")
//    public String newUser(Model model) {
//        System.out.println("called method newUser");
//        model.addAttribute("user", new UserDTO());
//        return "user";
//    }

//    @PostAuthorize("isAuthenticated() and #username == authentication.principal.username")
//    @GetMapping("/{name}/roles")
//    @ResponseBody
//    public String getRoles(@PathVariable("email") String email) {
//        System.out.println("called method getRoles");
//        User byEmail = userService.findByEmail(email);
//        return byEmail.getRole().name();
//    }

//    @PostMapping("/new")
//    public String saveUser(UserDTO userDTO, Model model) {
//        if (userService.save(userDTO)) {
//            return "redirect:/users";
//        } else {
//            model.addAttribute("user", userDTO);
//            return "user";
//        }
//    }

    @GetMapping
    @PostAuthorize("isAuthenticated()")
    public String profileUser(Model model, Principal principal) {
        if (principal == null) {
            throw new RuntimeException("You are not authorize");
        }
        User user = userService.findByEmail(principal.getName());

        UserDTO userDTO = UserDTO.builder()
                .email(user.getEmail())
                .lastname(user.getLastname())
                .firstname(user.getFirstname())
                .phone(user.getPhone())
                .build();

        model.addAttribute("userDTO", userDTO);

        return "profile";
    }


    @PostMapping
    @PostAuthorize("isAuthenticated()")
    public String update(@Valid UserDTO userDTO, Principal principal, BindingResult bindingResult) {
//        if (principal == null || !Objects.equals(principal.getName(), userDTO.getEmail())) {
//            throw new RuntimeException("You are not authorize");
//        }

        if (bindingResult.hasErrors()) {
            return "profile";
        }

        if (!Objects.equals(principal.getName(), userDTO.getEmail())) {
            if (userService.findByEmail(userDTO.getEmail()) != null) {
                bindingResult.rejectValue(
                        "email",
                        "email.exist",
                        "Email already exists");

                return "profile";
            }
        }

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()
                && !Objects.equals(userDTO.getPassword(), userDTO.getMatchingPassword())) {
            bindingResult.rejectValue(
                    "matchingPassword",
                    "password.matchingPassword",
                    "Different passwords");

            return "profile";
        }

        userService.updateProfile(userDTO);

        return "redirect:/profile";
    }
}
