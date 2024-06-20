package com.project.userservice.facade.admin;

import com.project.userservice.dto.UserDTO;
import com.project.userservice.entity.Role;
import com.project.userservice.entity.User;
import com.project.userservice.mapper.UserMapper;
import com.project.userservice.service.RoleService;
import com.project.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserAdminFacadeTest {

    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private RoleService roleService;

    @InjectMocks
    private UserAdminFacade userAdminFacade;

    private User user;
    private User user1;

    private User user2;
    private UserDTO userDTO;
    private UserDTO userDTO1;
    private UserDTO userDTO2;

    private Role role;

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

        user1 = User.builder()
                .id(2L)
                .name("Jane")
                .surname("Apple")
                .enabled(true)
                .password("password")
                .username("janeapple")
                .roles(new HashSet<>())
                .build();

        user2 = User.builder()
                .id(1L)
                .name("Jane")
                .surname("Apple")
                .enabled(true)
                .password("password")
                .username("janeapple")
                .roles(new HashSet<>())
                .build();


        userDTO = UserDTO.builder()
                .id(1L)
                .name("John")
                .surname("Doe")
                .password("password")
                .username("johndoe")
                .roles(new HashSet<>())
                .build();

        userDTO1 = UserDTO.builder()
                .id(2L)
                .name("Jane")
                .surname("Apple")
                .password("password")
                .username("janeapple")
                .roles(new HashSet<>())
                .build();

        userDTO2 = UserDTO.builder()
                .id(2L)
                .name("Jane")
                .surname("Apple")
                .password("password updated")
                .username("janeapple")
                .roles(new HashSet<>())
                .build();

        role = Role.builder()
                .id(1L)
                .name("ADMIN")
                .description("This is an admin Role")
                .build();

    }

    @Test
    void findAllUserTest(){

        List<User> users = List.of(user,user1);
        when(userService.findAllUsers()).thenReturn(users);

        when(userMapper.map(user)).thenReturn(userDTO);
        when(userMapper.map(user1)).thenReturn(userDTO1);

        List<UserDTO> resultList = userAdminFacade.findAllUser();

        assertEquals(2,resultList.size());
        assertEquals("johndoe",resultList.get(0).getUsername());
        assertFalse(resultList.isEmpty());

    }

    @Test
    void findUserTest(){

        when(userService.findUser(user.getId())).thenReturn(user);
        when(userMapper.map(user)).thenReturn(userDTO);
        UserDTO userDtoResult = userAdminFacade.findUser(user.getId());

        assertEquals(userDtoResult.getName(),userDTO.getName());
        assertEquals(userDtoResult.getId(),userDTO.getId());
        assertEquals(userDtoResult.getPassword(),userDTO.getPassword());
        assertEquals(userDtoResult.getUsername(),userDTO.getUsername());

    }

    @Test
    void saveUserTest(){

        when(userMapper.mapDto(userDTO)).thenReturn(user);
        when(userService.saveUser(user)).thenReturn(user);
        when(userMapper.map(user)).thenReturn(userDTO);
        UserDTO userDtoResult = userAdminFacade.saveUser(userDTO);

        assertEquals(userDtoResult.getUsername(),userDTO.getUsername());

    }

    @Test
    void updateUser(){

        when(userService.findUser(user1.getId())).thenReturn(user1);
        when(userMapper.mapDto(userDTO2)).thenReturn(user1);
        when(userService.saveUser(user1)).thenReturn(user1);
        when(userMapper.map(user1)).thenReturn(userDTO2);

        UserDTO userDtoResult = userAdminFacade.updateUser(user1.getId(),userDTO2);

        assertEquals(userDtoResult.getPassword(),userDTO2.getPassword());


    }

    @Test
    void deleteUserTest(){

        Long userId = 1L;

        willDoNothing().given(userService).deleteUser(userId);

        userAdminFacade.deleteUser(userId);

        verify(userService,times(1)).deleteUser(userId);
    }

    @Test
    void saveAndMapTest(){

        when(userService.saveUser(user)).thenReturn(user);
        when(userMapper.map(user)).thenReturn(userDTO);
        UserDTO userDtoResult = userAdminFacade.saveAndMap(user);

        assertEquals(userDtoResult.getUsername(),userDTO.getUsername());
    }

    @Test
    void assignRoleTest(){

        Long userId = 1L;
        Long roleId = 1L;

        when(userService.findUser(userId)).thenReturn(user);
        when(roleService.findRole(roleId)).thenReturn(role);

        userAdminFacade.assignRole(userId,roleId);

        verify(userService).findUser(userId);
        verify(roleService).findRole(roleId);
        verify(userService).saveUser(user);

        assertTrue(user.getRoles().contains(role));

    }

    @Test
    void removeRole(){

        Long userId = 1L;
        Long roleId = 1L;

        user2.getRoles().add(role);

        when(userService.findUser(userId)).thenReturn(user2);
        when(roleService.findRole(roleId)).thenReturn(role);

        userAdminFacade.removeRole(userId,roleId);

        verify(userService).findUser(userId);
        verify(roleService).findRole(roleId);
        verify(userService).saveUser(user2);

        assertFalse(user.getRoles().contains(role));



    }




}
