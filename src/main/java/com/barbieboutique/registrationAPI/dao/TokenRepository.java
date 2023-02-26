package com.barbieboutique.registrationAPI.dao;

import com.barbieboutique.registrationAPI.entity.Token;
import com.barbieboutique.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByToken(String token);

    Token findByUser(User user);
}
