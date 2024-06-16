package com.project.userservice.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.userservice.entity.Role;
import com.project.userservice.repository.RoleRepository;
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
public class RoleControllerTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private RoleRepository roleRepository;

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
    @WithMockUser(username = "admintest",roles = {"ADMIN"})
    void getAllRolesHttpRequest() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admin/roles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UFT8))
                .andExpect(jsonPath("$",hasSize(3)));


    }

    @Test
    @WithMockUser(username = "admintest",roles = {"ADMIN"})
    void getRoleHttpRequest()throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admin/roles/{id}",1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UFT8))
                .andExpect(jsonPath("$.name",is("ADMIN")));
    }


    @Test
    @WithMockUser(username = "admintest",roles = {"ADMIN"})
    void deleteRoleHttpRequest() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/admin/roles/{id}",3))
                .andExpect(status().isNoContent());

        Optional<Role> role = roleRepository.findById(3L);

        assertTrue(role.isEmpty());
    }



    @AfterEach
    public void setupAfterTransaction(){


        jdbc.execute(sqlDeleteUsersRoles);
        jdbc.execute(sqlDeleteUsers);
        jdbc.execute(sqlDeleteRoles);

    }


}
