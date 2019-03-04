package com.alexvak.urs.controllers;

import com.alexvak.urs.domain.User;
import com.alexvak.urs.exceptions.DuplicateUserFoundException;
import com.alexvak.urs.exceptions.UserNotFoundException;
import com.alexvak.urs.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserControllerTest {

    @Mock
    UserService userService;

    private MockMvc mockMvc;

    private User commonUser;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        UserController userController = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(ControllerExceptionHandler.class).build();

        commonUser = new User();
        commonUser.setId(1L);
        commonUser.setName("commonUser");
        commonUser.setEmail("commonUser@gmail.com");
        commonUser.setAddress("commonUser address");
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
    public void createUser() throws Exception {

        when(userService.createUser(any())).thenReturn(commonUser);

        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(asJsonString(commonUser)))
                .andExpect(status().isCreated())
                .andExpect(content().json("{'name': 'commonUser','address': 'commonUser address','email': 'commonUser@gmail.com'}"));

        verify(userService, times(1)).createUser(any());
    }

    @Test
    public void getUserById() throws Exception {

        when(userService.findById(commonUser.getId())).thenReturn(commonUser);

        mockMvc.perform(get("/api/user/{id}", commonUser.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json("{'name': 'commonUser','address': 'commonUser address','email': 'commonUser@gmail.com'}"));

        verify(userService, times(1)).findById(commonUser.getId());

    }

    @Test
    public void updateUser() throws Exception {
        when(userService.updateUser(commonUser.getId(), commonUser)).thenReturn(commonUser);

        mockMvc.perform(put("/api/user/{id}", commonUser.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(asJsonString(commonUser)))
                .andExpect(status().isOk());
        verify(userService, times(1)).updateUser(commonUser.getId(), commonUser);
        verifyNoMoreInteractions(userService);

    }

    @Test
    public void deleteUser() throws Exception {
        mockMvc.perform(delete("/api/user/{id}", commonUser.getId()))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(commonUser.getId());

    }

    @Test
    public void testUserNotFound() throws Exception {

        UserNotFoundException userNotFoundException = new UserNotFoundException(50L);
        when(userService.findById(50L)).thenThrow(userNotFoundException);

        mockMvc.perform(get("/api/user/50"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{'message':'User not found. ID: 50'}"));
    }

    @Test
    public void testDuplicateUserFoundException() throws Exception {

        DuplicateUserFoundException duplicateUserFoundException = new DuplicateUserFoundException("commonUser");
        when(userService.createUser(commonUser)).thenThrow(duplicateUserFoundException);


        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(asJsonString(commonUser)))
                .andExpect(status().isConflict())
                .andExpect(content().json("{'message':'Unable to create new user. A User with name commonUser already exist.'}"));

        verify(userService, times(1)).createUser(commonUser);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}