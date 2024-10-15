import com.fasterxml.jackson.databind.ObjectMapper;
import ir.isc.academy.wallet.controller.WalletController;
import ir.isc.academy.wallet.dto.responseDto.WalletResponseDTO;
import ir.isc.academy.wallet.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class WalletControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletController walletController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(walletController).build();
    }

    @Test
    void readWallet_shouldReturnWalletDetails() throws Exception {
        UUID userId = UUID.randomUUID();
        WalletResponseDTO walletResponseDTO = new WalletResponseDTO();
        walletResponseDTO.setWalletId(UUID.randomUUID());
        walletResponseDTO.setUserId(userId);
        walletResponseDTO.setBalance(new BigDecimal("1000.00"));

        when(walletService.readWallet(userId)).thenReturn(walletResponseDTO);

        mockMvc.perform(get("/api/v1/wallet/read")
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.balance").value("1000.00"));
    }

    @Test
    void readWallet_shouldReturnNotFound_whenWalletDoesNotExist() throws Exception {
        UUID userId = UUID.randomUUID();

        when(walletService.readWallet(userId)).thenThrow(new RuntimeException("Wallet not found"));

        mockMvc.perform(get("/api/v1/wallet/read")
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void readWallet_shouldReturnBadRequest_whenUserIdIsInvalid() throws Exception {
        mockMvc.perform(get("/api/v1/wallet/read")
                        .param("userId", "invalid-uuid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}