package com.barbieboutique.user.dto;


import com.barbieboutique.user.dto.customValidators.ValidEmail;
import com.barbieboutique.user.entity.Role;
import jakarta.validation.constraints.NotBlank;
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

    private Long id;

    @NotNull(message = "{email.NotNull}")
    @ValidEmail(message = "{email.ValidEmail}")
    @NotBlank(message = "{email.NotBlank}")
    private String email;

    @NotNull(message = "{password.NotNull}")
    @Size(min = 6, message = "{password.Size}")
    private String password;

    @NotNull(message = "{matchingPassword.NotNull}")
    private String matchingPassword;

    @NotNull(message = "{firstname.NotNull}")
    @NotBlank(message = "{firstname.NotBlank}")
    @Size(min = 2, message = "{firstname.Size}")
    private String firstname;

    @NotNull(message = "{lastname.NotNull}")
    @NotBlank(message = "{lastname.NotBlank}")
    @Size(min = 2, message = "{lastname.Size}")
    private String lastname;

    private String phone;

    private Role role;
}
