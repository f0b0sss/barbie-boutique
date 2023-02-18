package com.barbieboutique.user.dto;


import com.barbieboutique.user.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    @NotNull
    private Long id;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 6, message = "Password must be min 6 symbols")
    private String password;

    @NotNull
    @Size(min = 6, message = "Password must be min 6 symbols")
    private String matchingPassword;

    @NotNull
    @Size(min = 3, message = "{email.notEmpty}")
    private String firstname;

    @NotNull
    @Size(min = 3, message = "Lastname must be min 3 symbols")
    private String lastname;

    @NotNull
    @Size(min = 3, message = "Phone must be min 10 symbols")
    private String phone;

    private Role role;
}
