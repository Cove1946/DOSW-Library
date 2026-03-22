package edu.eci.dosw.tdd;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.dosw.tdd.controller.UserController;
import edu.eci.dosw.tdd.controller.dto.UserDTO;
import edu.eci.dosw.tdd.controller.mapper.UserMapper;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserMapper userMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void registerUser_shouldReturn201() throws Exception {
        UserDTO userDTO = new UserDTO("U1", "Lees");
        User user = new User("U1", "Lees");

        when(userMapper.toModel(any(UserDTO.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated());

        verify(userService, times(1)).registerUser(user);
    }

    @Test
    void getAllUsers_shouldReturn200() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(
                new User("U1", "Lees")
        ));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("U1"));
    }

    @Test
    void getUserById_shouldReturn200() throws Exception {
        when(userService.getUserById("U1"))
                .thenReturn(new User("U1", "Lees"));

        mockMvc.perform(get("/users/U1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("U1"))
                .andExpect(jsonPath("$.name").value("Lees"));
    }

    @Test
    void getUserById_shouldReturn404_whenNotFound() throws Exception {
        when(userService.getUserById("U99"))
                .thenThrow(new UserNotFoundException("User not found: U99"));

        mockMvc.perform(get("/users/U99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser_shouldReturn200() throws Exception {
        UserDTO updatedDTO = new UserDTO("U1", "Lees Updated");
        User updatedUser = new User("U1", "Lees Updated");

        when(userMapper.toModel(any(UserDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/users/U1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateUser("U1", updatedUser);
    }

    @Test
    void deleteUser_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/users/U1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser("U1");
    }
}