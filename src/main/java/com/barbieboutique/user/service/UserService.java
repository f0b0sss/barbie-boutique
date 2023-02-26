package com.barbieboutique.user.service;


import com.barbieboutique.exceptions.PasswordsNotEqualsException;
import com.barbieboutique.exceptions.UserAlreadyExistException;
import com.barbieboutique.user.dto.PasswordDto;
import com.barbieboutique.user.dto.UserDTO;
import com.barbieboutique.user.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User save(UserDTO userDTO) throws UserAlreadyExistException, PasswordsNotEqualsException;

    void save(User user);

    List<UserDTO> getAll();

    User findByEmail(String email);

    UserDTO getById(Long id);

    void updateProfile(UserDTO userDTO);

    void createPasswordResetTokenForUser(User user, String token);

    void changeUserPassword (User user, PasswordDto passwordDto) throws PasswordsNotEqualsException;
}
