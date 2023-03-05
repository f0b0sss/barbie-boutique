package com.barbieboutique.user.service;


import com.barbieboutique.exceptions.PasswordsNotEqualsException;
import com.barbieboutique.exceptions.UserAlreadyExistException;
import com.barbieboutique.exceptions.WrongOldPasswordException;
import com.barbieboutique.registrationAPI.dao.TokenRepository;
import com.barbieboutique.registrationAPI.entity.Token;
import com.barbieboutique.user.dao.UserRepository;
import com.barbieboutique.user.dto.PasswordDto;
import com.barbieboutique.user.dto.UpdateUserDTO;
import com.barbieboutique.user.dto.UserDTO;
import com.barbieboutique.user.entity.Status;
import com.barbieboutique.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;


    @Transactional
    @Override
    public User save(UserDTO userDTO) throws UserAlreadyExistException, PasswordsNotEqualsException {

        if (findByEmail(userDTO.getEmail()) != null) {
            throw new UserAlreadyExistException("There is an account with that email address: "
                    + userDTO.getEmail());
        }

        if (!Objects.equals(userDTO.getPassword(), userDTO.getMatchingPassword())) {
            throw new PasswordsNotEqualsException("Passwords don't match");
        }

        User user = User.builder()
                .lastname(userDTO.getLastname())
                .firstname(userDTO.getFirstname())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .phone(userDTO.getPhone())
                .status(Status.INACTIVE)
                .build();

        return userRepository.saveAndFlush(user);
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

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findFirstByEmail(email);
    }

    @Override
    public UserDTO getById(Long id) {
        User user = userRepository.findById(id).orElseThrow();

        UserDTO userDTO = toUserDTO(user);

        return userDTO;
    }

//    @Override
//    @Transactional
//    public void updateProfile(UserDTO userDTO) {
//        User savedUser = userRepository.findFirstByEmail(userDTO.getEmail());
//        if (savedUser == null) {
//            throw new RuntimeException("User not found by name " + userDTO.getPassword());
//        }
//
//        boolean isChanged = false;
//        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
//            savedUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
//            isChanged = true;
//        }
//
//        if (!Objects.equals(userDTO.getEmail(), savedUser.getEmail())) {
//            savedUser.setEmail(userDTO.getEmail());
//            isChanged = true;
//        }
//        if (!Objects.equals(userDTO.getPhone(), savedUser.getPhone())) {
//            savedUser.setPhone(userDTO.getPhone());
//            isChanged = true;
//        }
//        if (!Objects.equals(userDTO.getFirstname(), savedUser.getFirstname())) {
//            savedUser.setFirstname(userDTO.getFirstname());
//            isChanged = true;
//        }
//        if (!Objects.equals(userDTO.getLastname(), savedUser.getLastname())) {
//            savedUser.setLastname(userDTO.getLastname());
//            isChanged = true;
//        }
//
//        if (isChanged) {
//            userRepository.save(savedUser);
//        }
//    }

    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll().stream()
                .map(this::toUserDTO)
                .collect(Collectors.toList());
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


//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        User user = userRepository.findFirstByEmail(email);
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found with email: " + email);
//        }
//
//        return new CustomUserDetails(user);
//
////        re
////        List<GrantedAuthority> roles = new ArrayList<>();
////        roles.add(new SimpleGrantedAuthority(user.getRole().name()));
////
////        return new org.springframework.security.core.userdetails.User(
////                user.getEmail(),
////                user.getPassword(),
////                roles
////        );
//    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        Token myToken = tokenRepository.findByUser(user);

        myToken.setExpiryDate();
        myToken.setToken(token);

        tokenRepository.save(myToken);
    }

    @Override
    public void changeUserPassword(User user, PasswordDto passwordDto) throws PasswordsNotEqualsException {
        if (!Objects.equals(passwordDto.getNewPassword(), passwordDto.getMatchingPassword())) {
            throw new PasswordsNotEqualsException("Passwords don't match");
        }

        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));

        userRepository.save(user);
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
