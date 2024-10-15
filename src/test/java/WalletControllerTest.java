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
import java.math.BigInteger;
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

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(walletController).build();
    }

    @Test
    void readWallet_shouldReturnWalletDetails() throws Exception {
        String username = "testuser";
        WalletResponseDTO walletResponseDTO = new WalletResponseDTO();
        walletResponseDTO.setWalletId(UUID.randomUUID());
        walletResponseDTO.setAccountNumber(new BigInteger("12345678901234"));
        walletResponseDTO.setBalance(new BigDecimal("1000.0"));
        walletResponseDTO.setIban("IR-0000727379945");

        when(walletService.readWallet(username)).thenReturn(walletResponseDTO);

        mockMvc.perform(get("/api/v1/wallet/read")
                        .param("username", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.walletId").isNotEmpty())
                .andExpect(jsonPath("$.accountNumber").value("12345678901234"))
                .andExpect(jsonPath("$.balance").value("1000.0"))
                .andExpect(jsonPath("$.iban").value("IR-0000727379945"));
    }

    @Test
    void readWallet_shouldReturnNotFound_whenWalletDoesNotExist() throws Exception {
        String username = "nonexistentuser";

        when(walletService.readWallet(username)).thenThrow(new RuntimeException("Wallet not found"));

        mockMvc.perform(get("/api/v1/wallet/read")
                        .param("username", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}