import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ir.isc.academy.wallet.controller.UserController;
import ir.isc.academy.wallet.dto.ResponseMessage;
import ir.isc.academy.wallet.dto.requestDto.TransactionHistoryDto;
import ir.isc.academy.wallet.dto.requestDto.UserUpdateRequestDto;
import ir.isc.academy.wallet.dto.responseDto.UserResponseDTO;
import ir.isc.academy.wallet.exception.UsernameAlreadyTakenException;
import ir.isc.academy.wallet.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void readUser_shouldReturnUserDetails() throws Exception {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUsername("testuser");

        when(userService.readUser("testuser")).thenReturn(userResponseDTO);
        mockMvc.perform(get("/api/v1/user/read")
                        .param("username", "testuser")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void updateUser_shouldReturnSuccessMessage() throws Exception {
        UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto();
        userUpdateRequestDto.setUsername("testuser");
        when(userService.updateUser(any(UserUpdateRequestDto.class)))
                .thenReturn(new ResponseMessage("User updated successfully"));

        mockMvc.perform(put("/api/v1/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User updated successfully"));
    }

    @Test
    void deleteUser_shouldReturnSuccessMessage() throws Exception {
        when(userService.deleteUser("testuser"))
                .thenReturn(new ResponseMessage("User deleted successfully"));

        mockMvc.perform(delete("/api/v1/user/delete")
                        .param("username", "testuser")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User deleted successfully"));
    }

    @Test
    void userHistory_shouldReturnTransactionHistory() throws Exception {
        TransactionHistoryDto transactionHistoryDto = new TransactionHistoryDto();
        transactionHistoryDto.setTransactionId(UUID.fromString("c1af5a5d-a5ce-41f8-bb6a-dba8111d864f"));
        List<TransactionHistoryDto> transactionHistoryList = Collections.singletonList(transactionHistoryDto);

        when(userService.userTransactionHistory("testuser")).thenReturn(transactionHistoryList);

        mockMvc.perform(get("/api/v1/user/history")
                        .param("username", "testuser")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].transactionId").value("c1af5a5d-a5ce-41f8-bb6a-dba8111d864f"));
    }

}