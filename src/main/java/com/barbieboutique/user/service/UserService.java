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
    List<UserDTO> getAll();

    UserDTO getById(Long id);

    User findByEmail(String email);
    void updateData(UpdateUserDTO updateUserDTO) throws UserAlreadyExistException;

    void updatePassword(User user, PasswordDto passwordDto) throws PasswordsNotEqualsException, WrongOldPasswordException;
}
