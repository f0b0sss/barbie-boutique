package com.barbieboutique.registrationAPI.service;


import com.barbieboutique.registrationAPI.entity.Token;
import com.barbieboutique.user.entity.User;

import java.util.Optional;

public interface TokenService {
    Optional getUserByToken(String token);

    void createToken(User user, String token);

    Token getToken(String VerificationToken);

    String validateToken(String token);
}
