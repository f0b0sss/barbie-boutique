package com.barbieboutique.user.dto.mapper;

import com.barbieboutique.user.dto.UserDTO;
import com.barbieboutique.user.entity.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.factory.Mappers;

import java.util.List;

public interface UserMapper {
    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    User toUser(UserDTO userDTO);

    @InheritInverseConfiguration
    UserDTO toUserDTO(User user);

    List<User> toUserList(List<UserDTO> userDTOS);

    List<UserDTO> toUserDTOList(List<User> users);
}
