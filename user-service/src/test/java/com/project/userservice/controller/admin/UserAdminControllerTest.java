package com.project.userservice.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.userservice.dto.UserDTO;
import com.project.userservice.entity.Role;
import com.project.userservice.entity.User;
import com.project.userservice.facade.admin.UserAdminFacade;
import com.project.userservice.mapper.UserMapper;
import com.project.userservice.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.awaitility.Awaitility.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
public class UserAdminControllerTest {

    private static MockHttpServletRequest request;

    @PersistenceContext
    private EntityManager entityManager;

    @Mock
    private UserAdminFacade userAdminFacadeMock;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAdminFacade userAdminFacade;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;



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

    @Autowired
    private UserMapper userMapper;



    public static final MediaType APPLICATION_JSON_UFT8 = MediaType.APPLICATION_JSON;




    @BeforeAll
    public static void setup(){


        request = new MockHttpServletRequest();

        request.setParameter("username","usertest");

        request.setParameter("name","user");

        request.setParameter("password","$2a$10$U4dtBxoInnEYmbeU3elni.dNtvBlhcNksgdYs4XtUetxvovNweEcC");

        //request.setParameter("enabled","true");

        request.setParameter("surname","Test");

    }

    @BeforeEach
    public void setupDatabase(){

        /*mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                        .apply(springSecurity())
                                .build();*/





        /*jdbc.execute("insert into _user(id,enabled,_name,_password,surname,_username) " +
                "values (1,true,'admin', '$2a$10$U4dtBxoInnEYmbeU3elni.dNtvBlhcNksgdYs4XtUetxvovNweEcC','Test', 'admintest')," +
                "(2,true,'manager', '$2a$10$U4dtBxoInnEYmbeU3elni.dNtvBlhcNksgdYs4XtUetxvovNweEcC','Test', 'managertest')," +
                "(3,true,'cashier', '$2a$10$U4dtBxoInnEYmbeU3elni.dNtvBlhcNksgdYs4XtUetxvovNweEcC','Test', 'cashiertest')");
        jdbc.execute("insert into _role(id,_description,_name) values(1,'This is a admin Role','ADMIN')," +
                                                                        "(2,'This is a manager Role','MANAGER')," +
                                                                        "(3,'This is a cashier Role','CASHIER')");
        jdbc.execute("insert into user_role(user_id,role_id) values (1,1),(2,2),(3,3)");*/
        jdbc.execute(sqlAddUsers);
        jdbc.execute(sqlAddRoles);
        jdbc.execute(sqlAddUsersRoles);


    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @WithMockUser(username = "admintest",roles = {"ADMIN"})
    void getAllStudentHttpRequest() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admin/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UFT8))
                .andExpect(jsonPath("$",hasSize(3)));

    }


    @Test
    @WithMockUser(username = "admintest",roles = {"ADMIN"})
    void getUserHttpRequest() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admin/users/{id}",1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UFT8))
                .andExpect(jsonPath("$.surname",is("Test")));

    }

    /*@Test
    @WithMockUser(username = "admintest",roles = {"ADMIN"})
    void saveUserHttpRequest() throws Exception{

        UserDTO userDTO = UserDTO.builder()
                        //.id(null)
                        .name("user")
                        .surname("Test")
                        .password("dummy")
                        .username("usertest")
                        .build();

        UserDTO savedUser = userAdminFacade.saveUser(userDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedUser))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.name",is("user")));


    }*/

    @Test
    @WithMockUser(username = "admintest",roles = {"ADMIN"})
    void updateUserHttpRequest() throws Exception{

        UserDTO user = userAdminFacade.findUser(3L);
        user.setPassword("dummy");


        assertEquals("cashiertest", user.getUsername());

        user.setUsername("usertest");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/admin/users/{id}",3L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isAccepted());

        Optional<User> user1 = userRepository.findById(3L);

        assertEquals("usertest",user1.get().getUsername());
    }


    @Test
    @WithMockUser(username = "admintest",roles = {"ADMIN"})
    void deleteUserHttpRequest() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/admin/users/{id}",3))
                .andExpect(status().isNoContent());


        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admin/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UFT8))
                .andExpect(jsonPath("$",hasSize(2)));
    }

    @Test
    @WithMockUser(username = "admintest",roles = {"ADMIN"})
    void assignRoleUserHttpRequest() throws Exception{

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/users/{id}/role/{roleId}",3,2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
                //.andExpect(jsonPath("$.roles",hasSize(2)));

        assertEquals(2,userRepository.findById(3L).get().getRoles().size());


    }

    @Test
    @WithMockUser(username = "admintest",roles = {"ADMIN"})
    void removeRoleUserHttpRequest() throws Exception{

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/admin/users/{id}/role/{roleId}",3,3))
                .andExpect(status().isOk());

        assertTrue(userRepository.findById(3L).get().getRoles().isEmpty());

    }



    @AfterEach
    public void setupAfterTransaction(){
        /*jdbc.execute("delete from user_role");
        jdbc.execute("delete from _user");
        jdbc.execute("delete from _role");*/


        jdbc.execute(sqlDeleteUsersRoles);
        jdbc.execute(sqlDeleteUsers);
        jdbc.execute(sqlDeleteRoles);

    }
















}
