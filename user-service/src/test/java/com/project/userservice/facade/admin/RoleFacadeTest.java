package com.project.userservice.facade.admin;

import com.project.userservice.dto.RoleDTO;
import com.project.userservice.entity.Role;
import com.project.userservice.mapper.RoleMapper;
import com.project.userservice.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class RoleFacadeTest {

    @Mock
    private RoleService roleService;
    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleFacade roleFacade;

    private Role role;
    private Role role1;
    private RoleDTO roleDTO;
    private RoleDTO roleDTO1;
    private RoleDTO roleDTO2;


    @BeforeEach
    void beforeEach(){

        role = Role.builder()
                .id(1L)
                .name("MANAGER")
                .description("This is a manager Role")
                .build();

        role1 = Role.builder()
                .id(2L)
                .name("CASHIER")
                .description("This is a cashier Role")
                .build();

        roleDTO = RoleDTO.builder()
                .id(1L)
                .name("MANAGER")
                .description("This is a manager Role Updated")
                .build();

        roleDTO1 = RoleDTO.builder()
                .id(1L)
                .name("MANAGER")
                .description("This is a manager Role")
                .build();

        roleDTO2 = RoleDTO.builder()
                .id(2L)
                .name("CASHIER")
                .description("This is a cashier Role")
                .build();

    }

    @Test
    void findAllRolesTest(){

        List<Role> roles = List.of(role,role1);
        when(roleService.findAllRoles()).thenReturn(roles);

        when(roleMapper.map(role)).thenReturn(roleDTO1);
        when(roleMapper.map(role1)).thenReturn(roleDTO2);

        List<RoleDTO> resultList = roleFacade.findAllRoles();

        assertEquals(2,resultList.size());
        assertFalse(resultList.isEmpty());

    }

    @Test
    void findRoleTest(){
        when(roleService.findRole(role.getId())).thenReturn(role);
        when(roleMapper.map(role)).thenReturn(roleDTO1);
        RoleDTO roleDtoResult = roleFacade.findRole(role.getId());

        assertEquals(roleDtoResult.getId(),roleDTO1.getId());
        assertEquals(roleDtoResult.getName(),roleDTO1.getName());
        assertEquals(roleDtoResult.getDescription(),roleDTO1.getDescription());

    }

    @Test
    void saveRoleTest(){

        when(roleMapper.mapDto(roleDTO1)).thenReturn(role);
        when(roleService.saveRole(role)).thenReturn(role);
        when(roleMapper.map(role)).thenReturn(roleDTO1);
        RoleDTO roleDtoResult = roleFacade.saveRole(roleDTO1);

        assertEquals(roleDtoResult.getId(),roleDTO1.getId());
        assertEquals(roleDtoResult.getName(),roleDTO1.getName());
        assertEquals(roleDtoResult.getDescription(),roleDTO1.getDescription());

    }



    @Test
    void updateRoleTest(){

        when(roleService.findRole(role.getId())).thenReturn(role);
        when(roleMapper.map(role)).thenReturn(roleDTO);
        when(roleMapper.mapDto(roleDTO)).thenReturn(role);
        when(roleService.saveRole(role)).thenReturn(role);
        RoleDTO roleDtoResult = roleFacade.updateRole(role.getId(),roleDTO);
        assertEquals(roleDtoResult.getDescription(),roleDTO.getDescription());
    }

    @Test
    void deleteRoleTest(){

        Long roleId = 1L;

        doNothing().when(roleService).deleteRole(roleId);

        roleFacade.deleteRole(roleId);

        verify(roleService,times(1)).deleteRole(roleId);
    }






}
