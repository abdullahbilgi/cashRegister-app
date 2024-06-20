package com.project.userservice.facade;

import com.project.userservice.dto.UserDTO;
import com.project.userservice.entity.User;
import com.project.userservice.mapper.UserMapper;
import com.project.userservice.service.UserService;
import com.project.userservice.util.GeneralUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserFacadeTest {

    @Mock
    private  UserDetailsService userDetailsService;
    @Mock
    private  UserService userService;
    @Mock
    private  UserMapper userMapper;

    @InjectMocks
    private UserFacade userFacade;

    private User user;
    private User savedUser;
    private UserDTO userDTO;
    private UserDTO userDTO1;

    private MockedStatic<GeneralUtil> mockedGeneralUtil;


    @BeforeEach
    void beforeEach(){

        user = User.builder()
                .id(1L)
                .name("John")
                .surname("Doe")
                .username("johndoe")
                .enabled(true)
                .password("password")
                .roles(new HashSet<>())
                .build();

        savedUser = User.builder()
                .id(1L)
                .name("John")
                .surname("Doe Updated")
                .username("johndoe")
                .enabled(true)
                .password("password")
                .roles(new HashSet<>())
                .build();

        userDTO = UserDTO.builder()
                .id(1L)
                .name("John")
                .surname("Doe")
                .username("johndoe")
                .build();

        userDTO1 = UserDTO.builder()
                .id(1L)
                .name("John")
                .surname("Doe Updated")
                .username("johndoe")
                .build();



        /*//extractUser method
        String username = "johndoe";
        mockStatic(GeneralUtil.class);
        when(GeneralUtil.extractUsername()).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(user);*/

        String username = "johndoe";
        mockedGeneralUtil = mockStatic(GeneralUtil.class);
        mockedGeneralUtil.when(GeneralUtil::extractUsername).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(user);

    }

    @Test
    void findUserByUsernameTest(){


        when(userMapper.map(user)).thenReturn(userDTO);

        UserDTO userDTtoResult = userFacade.findUserByUsername();

        assertNotNull(userDTtoResult);
        assertEquals(userDTtoResult.getUsername(),userDTO.getUsername());

    }

    @Test
    void updateUserTest(){

        when(userMapper.mapDto(userDTO1)).thenReturn(savedUser);
        when(userService.saveUser(savedUser)).thenReturn(savedUser);
        when(userMapper.map(savedUser)).thenReturn(userDTO1);

        UserDTO userDtoResult = userFacade.updateUser(userDTO1);

        assertEquals(userDtoResult.getSurname(),userDTO1.getSurname());


    }

    @Test
    void deleteUserTest(){

        userFacade.deleteUser();

        assertFalse(user.isEnabled());

    }

    @AfterEach
    void afterEach(){
        mockedGeneralUtil.close();

    }




}
