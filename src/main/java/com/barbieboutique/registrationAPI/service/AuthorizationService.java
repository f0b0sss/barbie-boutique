package com.barbieboutique.registrationAPI.service;

import com.barbieboutique.exceptions.PasswordsNotEqualsException;
import com.barbieboutique.exceptions.UserAlreadyExistException;
import com.barbieboutique.user.dto.PasswordDto;
import com.barbieboutique.user.dto.UserDTO;
import com.barbieboutique.user.entity.User;

public interface AuthorizationService {

    User findByEmail(String email);

    User save(UserDTO userDTO) throws UserAlreadyExistException, PasswordsNotEqualsException;

    void save(User user);

    void createPasswordResetTokenForUser(User user, String token);

    void changeUserPassword(User user, PasswordDto passwordDto) throws PasswordsNotEqualsException;

}
