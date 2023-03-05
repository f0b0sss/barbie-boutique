package com.barbieboutique.user.service;


import com.barbieboutique.exceptions.PasswordsNotEqualsException;
import com.barbieboutique.exceptions.UserAlreadyExistException;
import com.barbieboutique.exceptions.WrongOldPasswordException;
import com.barbieboutique.user.dto.PasswordDto;
import com.barbieboutique.user.dto.UpdateUserDTO;
import com.barbieboutique.user.dto.UserDTO;
import com.barbieboutique.user.entity.User;

import java.util.List;

//public interface UserService extends UserDetailsService {
public interface UserService {
    User save(UserDTO userDTO) throws UserAlreadyExistException, PasswordsNotEqualsException;

    void save(User user);

    void updateData(UpdateUserDTO updateUserDTO) throws UserAlreadyExistException;

    List<UserDTO> getAll();

    User findByEmail(String email);

    UserDTO getById(Long id);

//    void updateProfile(UserDTO userDTO);

    void createPasswordResetTokenForUser(User user, String token);

    void changeUserPassword (User user, PasswordDto passwordDto) throws PasswordsNotEqualsException;

    void updatePassword(User user, PasswordDto passwordDto)throws PasswordsNotEqualsException, WrongOldPasswordException;
}
