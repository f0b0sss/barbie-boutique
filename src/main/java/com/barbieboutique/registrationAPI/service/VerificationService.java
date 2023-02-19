package com.barbieboutique.registrationAPI.service;


import com.barbieboutique.registrationAPI.entity.VerificationToken;
import com.barbieboutique.user.entity.User;

public interface VerificationService {
    User getUser(String verificationToken);

    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken);
}
