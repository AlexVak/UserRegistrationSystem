package com.alexvak.urs.controllers;

import com.alexvak.urs.domain.User;
import com.alexvak.urs.exceptions.UserNotFoundException;
import com.alexvak.urs.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserControllerTest {

    @Mock
    UserService userService;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        UserController userController = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(ControllerExceptionHandler.class).build();
    }

    @Test
    public void listAllUsers() throws Exception {
        //given
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        List<User> userList = Arrays.asList(user1, user2);

        //when
        when(userService.getAllUsers()).thenReturn(userList);
        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/user/"))
                .andExpect(status().isOk());

        //then
        verify(userService, times(2)).getAllUsers();
    }

    @Test
    public void createUser() {
    }

    @Test
    public void getUserById() {
    }

    @Test
    public void updateUser() {
    }

    @Test
    public void deleteUser() {
    }

    @Test
    public void testUserNotFound() throws Exception {

        UserNotFoundException userNotFoundException = new UserNotFoundException(50L);
        when(userService.findById(50L)).thenThrow(userNotFoundException);

        mockMvc.perform(get("/api/user/50"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{'message':'User not found. ID: 50'}"));
    }
}