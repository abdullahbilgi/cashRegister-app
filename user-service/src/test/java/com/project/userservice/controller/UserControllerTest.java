package com.project.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.userservice.dto.UserDTO;
import com.project.userservice.entity.Role;
import com.project.userservice.entity.User;
import com.project.userservice.facade.UserFacade;
import com.project.userservice.repository.RoleRepository;
import com.project.userservice.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${sql.script.create.users}")
    private String sqlAddUsers;

    @Value("${sql.script.create.roles}")
    private String sqlAddRoles;

    @Value("${sql.script.create.user_role}")
    private String sqlAddUsersRoles;

    @Value("${sql.script.delete.users}")
    private String sqlDeleteUsers;

    @Value("${sql.script.delete.roles}")
    private String sqlDeleteRoles;

    @Value("${sql.script.delete.user_role}")
    private String sqlDeleteUsersRoles;

    public static final MediaType APPLICATION_JSON_UFT8 = MediaType.APPLICATION_JSON;

    @BeforeEach
    public void setupDatabase(){

        jdbc.execute(sqlAddUsers);
        jdbc.execute(sqlAddRoles);
        jdbc.execute(sqlAddUsersRoles);

    }

    @Test
    @WithMockUser(username = "managertest",roles = {"MANAGER"})
    void getUserHttpRequest() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is("manager")))
                .andExpect(jsonPath("$.username",is("managertest")));

    }

    @Test
    @WithMockUser(username = "managertest",roles = {"MANAGER"})
    void updateUserHttpRequest() throws Exception{

        UserDTO userDTO = userFacade.findUserByUsername();
        userDTO.setPassword("dummy");
        userDTO.setName("abc");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isAccepted());

        Optional<User> updatedUser = userRepository.findByUsernameEagerly("managertest");

        assertEquals(updatedUser.get().getName(),userDTO.getName());
    }

    @Test
    @WithMockUser(username = "managertest",roles = {"MANAGER"})
    void deleteUserHttpRequest() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users"))
                .andExpect(status().isNoContent());

        Optional<User> user = userRepository.findByUsername("managertest");

        assertEquals(false,user.get().getEnabled());
    }


    @AfterEach
    public void setupAfterTransaction(){


        jdbc.execute(sqlDeleteUsersRoles);
        jdbc.execute(sqlDeleteUsers);
        jdbc.execute(sqlDeleteRoles);

    }
}
