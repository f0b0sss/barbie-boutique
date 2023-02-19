package com.barbieboutique.registrationAPI.service;


import com.barbieboutique.registrationAPI.dao.VerificationTokenRepository;
import com.barbieboutique.registrationAPI.entity.VerificationToken;
import com.barbieboutique.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VerificationServiceImpl implements VerificationService {
    private final VerificationTokenRepository tokenRepository;

    @Override
    public User getUser(String verificationToken) {
        User user = tokenRepository.findByToken(verificationToken).getUser();
        return user;
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(token, user);
        tokenRepository.save(verificationToken);
    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }
}
