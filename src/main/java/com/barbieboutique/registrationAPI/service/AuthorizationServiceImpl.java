package com.barbieboutique.registrationAPI.service;

import com.barbieboutique.exceptions.PasswordsNotEqualsException;
import com.barbieboutique.exceptions.UserAlreadyExistException;
import com.barbieboutique.registrationAPI.dao.TokenRepository;
import com.barbieboutique.registrationAPI.entity.Token;
import com.barbieboutique.user.dao.UserRepository;
import com.barbieboutique.user.dto.PasswordDto;
import com.barbieboutique.user.dto.UserDTO;
import com.barbieboutique.user.entity.Status;
import com.barbieboutique.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class AuthorizationServiceImpl implements AuthorizationService{

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findByEmail(String email) {
        return userRepository.findFirstByEmail(email);
    }

    @Transactional
    @Override
    public User save(UserDTO userDTO) throws UserAlreadyExistException, PasswordsNotEqualsException {

        if (userRepository.findFirstByEmail(userDTO.getEmail()) != null) {
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
    public void save(User user) {
        userRepository.save(user);
    }

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
}
