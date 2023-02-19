package com.barbieboutique.registrationAPI.dao;

import com.barbieboutique.registrationAPI.entity.VerificationToken;
import com.barbieboutique.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository   extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);
}
