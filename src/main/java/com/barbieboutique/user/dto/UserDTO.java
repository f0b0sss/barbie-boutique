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

    @NotNull(message = "{email.NotNull}")
    @Email
    private String email;

    @NotNull(message = "{password.NotNull}")
    @Size(min = 6, message = "{password.Size}")
    private String password;

    @NotNull(message = "{matchingPassword.NotNull}")
    private String matchingPassword;

    @NotNull(message = "{firstname.NotNull}")
    @Size(min = 2, message = "{firstname.Size}")
    private String firstname;

    @NotNull(message = "{lastname.NotNull}")
    @Size(min = 2, message = "{lastname.Size}")
    private String lastname;

    @NotNull(message = "{phone.NotNull}")
    @Size(min = 10, message = "{phone.Size}")
    private String phone;

    private Role role;
}
