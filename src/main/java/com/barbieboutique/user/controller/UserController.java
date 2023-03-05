package com.barbieboutique.user.controller;


import com.barbieboutique.exceptions.PasswordsNotEqualsException;
import com.barbieboutique.exceptions.UserAlreadyExistException;
import com.barbieboutique.exceptions.WrongOldPasswordException;
import com.barbieboutique.user.dto.PasswordDto;
import com.barbieboutique.user.dto.UpdateUserDTO;
import com.barbieboutique.user.entity.CustomUserDetails;
import com.barbieboutique.user.entity.User;
import com.barbieboutique.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/profile")
public class UserController {
    private final UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public String profile(Model model, Principal principal) {
        if (principal == null) {
            throw new RuntimeException("You are not authorize");
        }
        User user = userService.findByEmail(principal.getName());

        UpdateUserDTO updateUserDTO = UpdateUserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .lastname(user.getLastname())
                .firstname(user.getFirstname())
                .phone(user.getPhone())
                .build();

        model.addAttribute("updateUserDTO", updateUserDTO);

        return "profile";
    }

    @Transactional
    @PostMapping
    @PostAuthorize("isAuthenticated()")
    public String updateMainInfo(@AuthenticationPrincipal CustomUserDetails principal,
                                 @Valid UpdateUserDTO updateUserDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "profile";
        }

        try {
            userService.updateData(updateUserDTO);
        } catch (UserAlreadyExistException e) {
            bindingResult.rejectValue("email", "email.exist",
                    "Email already exists");
            return "profile";
        }

        principal.setEmail(updateUserDTO.getEmail());

        return "redirect:/profile";
    }

    @GetMapping("/password")
    @PostAuthorize("isAuthenticated()")
    public String editPassword(Model model) {
        model.addAttribute("passwordDto", new PasswordDto());

        return "edit-password";
    }

    @Transactional
    @PostMapping("/updatePassword")
    @PostAuthorize("isAuthenticated()")
    public String updatePassword(Principal principal, @Valid PasswordDto passwordDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit-password";
        }

        User user = userService.findByEmail(principal.getName());


        try {
            userService.updatePassword(user, passwordDto);
        } catch (PasswordsNotEqualsException e) {
            bindingResult.rejectValue("matchingPassword", "password.matchingPassword",
                    "Passwords don't match");
            return "edit-password";
        } catch (WrongOldPasswordException e){
            bindingResult.rejectValue("oldPassword", "password.oldPassword",
                    "Passwords don't match");
            return "edit-password";
        }


        return "redirect:/profile";
    }

}
