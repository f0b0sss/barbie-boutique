package com.barbieboutique.user.service;


import com.barbieboutique.exceptions.PasswordsNotEqualsException;
import com.barbieboutique.exceptions.UserAlreadyExistException;
import com.barbieboutique.exceptions.WrongOldPasswordException;
import com.barbieboutique.user.dao.UserRepository;
import com.barbieboutique.user.dto.PasswordDto;
import com.barbieboutique.user.dto.UpdateUserDTO;
import com.barbieboutique.user.dto.UserDTO;
import com.barbieboutique.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll().stream()
                .map(this::toUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getById(Long id) {
        User user = userRepository.findById(id).orElseThrow();

        UserDTO userDTO = toUserDTO(user);

        return userDTO;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findFirstByEmail(email);
    }

    @Override
    public void updateData(UpdateUserDTO updateUserDTO) throws UserAlreadyExistException {
        User existingUser = userRepository.findById(updateUserDTO.getId()).get();

        if (!updateUserDTO.getEmail().equals(existingUser.getEmail()) &&
                findByEmail(updateUserDTO.getEmail()) != null) {
            throw new UserAlreadyExistException("There is an account with that email address: "
                    + updateUserDTO.getEmail());
        }

        User user = User.builder()
                .id(updateUserDTO.getId())
                .lastname(updateUserDTO.getLastname())
                .firstname(updateUserDTO.getFirstname())
                .email(updateUserDTO.getEmail())
                .phone(updateUserDTO.getPhone())
                .enabled(existingUser.isEnabled())
                .status(existingUser.getStatus())
                .password(existingUser.getPassword())
                .role(existingUser.getRole())
                .build();

        userRepository.save(user);
    }

    private UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .lastname(user.getLastname())
                .firstname(user.getFirstname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .build();
    }

    @Override
    public void updatePassword(User user, PasswordDto passwordDto) throws PasswordsNotEqualsException, WrongOldPasswordException {
        if (!passwordEncoder.matches(passwordDto.getOldPassword(), user.getPassword())) {
            throw new WrongOldPasswordException("Incorrect Old Password");
        }

        if (!Objects.equals(passwordDto.getNewPassword(), passwordDto.getMatchingPassword())) {
            throw new PasswordsNotEqualsException("Passwords don't match");
        }

        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));

        userRepository.save(user);
    }
}
