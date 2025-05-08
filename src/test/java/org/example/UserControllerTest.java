package org.example;

import org.example.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void createUser_ValidInput_ReturnsCreatedUser() throws Exception {
        CreateUserRequestDto requestDto = new CreateUserRequestDto();
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");
        requestDto.setEmail("john.doe@example.com");

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(1L);
        responseDto.setFirstName("John");
        responseDto.setLastName("Doe");
        responseDto.setEmail("john.doe@example.com");

        when(userService.createUser(any(CreateUserRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john.doe@example.com"));

        verify(userService).createUser(any(CreateUserRequestDto.class));
    }

    @Test
    void getUserById_ExistingId_ReturnsUser() throws Exception {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(1L);
        responseDto.setFirstName("John");
        responseDto.setLastName("Doe");
        responseDto.setEmail("john.doe@example.com");

        when(userService.getUserById(1L)).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john.doe@example.com"));

        verify(userService).getUserById(eq(1L));
    }

    @Test
    void updateUser_ExistingId_ReturnsUpdatedUser() throws Exception {
        long userId = 1L;
        UpdateUserRequestDto updateRequestDto = new UpdateUserRequestDto();
        updateRequestDto.setFirstName("Jane");
        updateRequestDto.setLastName("Smith");
        updateRequestDto.setEmail("jane.smith@example.com");

        UserResponseDto updatedResponseDto = new UserResponseDto();
        updatedResponseDto.setId(userId);
        updatedResponseDto.setFirstName("Jane");
        updatedResponseDto.setLastName("Smith");
        updatedResponseDto.setEmail("jane.smith@example.com");

        when(userService.updateUser(eq(userId), any(UpdateUserRequestDto.class))).thenReturn(updatedResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Jane"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Smith"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("jane.smith@example.com"));

        verify(userService).updateUser(eq(userId), any(UpdateUserRequestDto.class));
    }

    @Test
    void deleteUser_ExistingId_ReturnsNoContent() throws Exception {
        long userId = 1L;

        Mockito.doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(eq(userId));
    }

    @Test
    void getUserById_NonExistingId_ReturnsNotFound() throws Exception {
        long nonExistingId = 99L;
        when(userService.getUserById(nonExistingId)).thenThrow(new NoSuchElementException());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/" + nonExistingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userService).getUserById(eq(nonExistingId));
    }

    @Test
    void updateUser_NonExistingId_ReturnsNotFound() throws Exception {
        long nonExistingId = 99L;
        UpdateUserRequestDto updateRequestDto = new UpdateUserRequestDto();
        updateRequestDto.setFirstName("Jane");
        updateRequestDto.setLastName("Smith");
        updateRequestDto.setEmail("jane.smith@example.com");

        when(userService.updateUser(eq(nonExistingId), any(UpdateUserRequestDto.class))).thenThrow(new NoSuchElementException());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/" + nonExistingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDto)))
                .andExpect(status().isNotFound());

        verify(userService).updateUser(eq(nonExistingId), any(UpdateUserRequestDto.class));
    }

    @Test
    void deleteUser_NonExistingId_ReturnsNotFound() throws Exception {
        long nonExistingId = 99L;
        Mockito.doThrow(new NoSuchElementException()).when(userService).deleteUser(nonExistingId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/" + nonExistingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userService).deleteUser(eq(nonExistingId));
    }
}