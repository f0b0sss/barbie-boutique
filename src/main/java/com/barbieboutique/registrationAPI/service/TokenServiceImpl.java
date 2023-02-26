package com.barbieboutique.registrationAPI.service;


import com.barbieboutique.registrationAPI.dao.TokenRepository;
import com.barbieboutique.registrationAPI.entity.Token;
import com.barbieboutique.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;

    @Override
    public Optional getUserByToken(String token) {
        User user = tokenRepository.findByToken(token).getUser();
        return Optional.ofNullable(user);
    }

    @Override
    public void createToken(User user, String token) {
        Token verificationToken = new Token(token, user);
        tokenRepository.save(verificationToken);
    }

    @Override
    public Token getToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    @Override
    public String validateToken(String token) {
        final Token passToken = tokenRepository.findByToken(token);

        return !isTokenFound(passToken) ? "invalidToken"
                : isTokenExpired(passToken) ? "expired"
                : null;
    }

    private boolean isTokenFound(Token passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(Token passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }
}
