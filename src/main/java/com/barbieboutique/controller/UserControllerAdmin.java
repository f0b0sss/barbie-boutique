package com.barbieboutique.controller;


import com.barbieboutique.user.dto.UserDTO;
import com.barbieboutique.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserControllerAdmin {
    private final UserService userService;


    @GetMapping
//    @PreAuthorize("hasAuthority()")
    public String users(Model model) {
        model.addAttribute("users", userService.getAll());
        return "users";
    }

    @GetMapping("/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        UserDTO userDTO = userService.getById(id);

        model.addAttribute("userDTO", userDTO);

        return "user";
    }
}
