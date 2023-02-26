package com.barbieboutique.user.dto;

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
public class PasswordDto {

    private String oldPassword;

    private String token;

    @NotNull(message = "{password.NotNull}")
    @Size(min = 6, message = "{password.Size}")
    private String newPassword;

    @NotNull(message = "{matchingPassword.NotNull}")
    private String matchingPassword;
}
