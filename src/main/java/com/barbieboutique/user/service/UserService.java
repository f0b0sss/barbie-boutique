package com.barbieboutique.user.service;


import com.barbieboutique.user.dto.UserDTO;
import com.barbieboutique.user.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    boolean save(UserDTO userDTO);

    void save(User user);

    List<UserDTO> getAll();

    User findByEmail(String email);

    UserDTO getById(Long id);

    void updateProfile(UserDTO userDTO);
}
