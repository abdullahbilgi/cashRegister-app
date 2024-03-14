package com.project.userservice.facade;

import com.project.userservice.dto.UserDTO;
import com.project.userservice.entity.User;
import com.project.userservice.mapper.UserMapper;
import com.project.userservice.service.UserService;
import com.project.userservice.util.GeneralUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final UserMapper userMapper;

    public UserDTO findUserByUsername(){
        User user = extractUser();

        UserDTO userDTO =userMapper.map(user);
        userDTO.setRoles(null); // hide the roles

        return userDTO;
    }

    public UserDTO updateUser(UserDTO userDTO){
        User user = extractUser();

        User savingUser = userMapper.mapDto(userDTO);
        savingUser.setId(user.getId());
        savingUser.setRoles(user.getRoles());

        User savedUser = userService.saveUser(savingUser); //Protected roles

        return userMapper.map(savedUser);
    }

    public void deleteUser(){
        User user = extractUser();
        user.setEnabled(false);
        userService.saveUser(user);
    }



    private User extractUser(){
        String username = GeneralUtil.extractUsername();

        return (User)userDetailsService.loadUserByUsername(username);

    }



}
