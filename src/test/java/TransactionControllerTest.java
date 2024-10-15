import com.fasterxml.jackson.databind.ObjectMapper;
import ir.isc.academy.wallet.controller.TransactionController;
import ir.isc.academy.wallet.dto.requestDto.TransactionRequestDto;
import ir.isc.academy.wallet.exception.InsufficientFundsException;
import ir.isc.academy.wallet.service.TransactionService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    void deposit_shouldReturnSuccessMessage() throws Exception {
        TransactionRequestDto requestDto = new TransactionRequestDto();
        requestDto.setAmount(new BigDecimal("100000"));
        requestDto.setSenderAccountNumber(new BigInteger("1234567890123"));
        requestDto.setRecipientAccountNumber(new BigInteger("1000000000012"));
        requestDto.setSenderIban("IR-0000727379956");
        requestDto.setType("DEPOSIT");
        requestDto.setDescription("Deposit to wallet");

        when(transactionService.deposit(any(TransactionRequestDto.class)))
                .thenReturn("Deposit successful");

        mockMvc.perform(post("/api/v1/transaction/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Deposit successful"));
    }

    @Test
    void withdrawal_shouldReturnSuccessMessage() throws Exception {
        TransactionRequestDto requestDto = new TransactionRequestDto();
        requestDto.setAmount(new BigDecimal("100000"));
        requestDto.setSenderAccountNumber(new BigInteger("1234567890123"));
        requestDto.setRecipientAccountNumber(new BigInteger("1000000000012"));
        requestDto.setSenderIban("IR-0000727379956");
        requestDto.setType("WITHDRAWAL");
        requestDto.setDescription("Withdrawal from wallet");

        when(transactionService.withdraw(any(TransactionRequestDto.class)))
                .thenReturn("Withdrawal successful");

        mockMvc.perform(post("/api/v1/transaction/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Withdrawal successful"));
    }

    @Test
    void withdrawal_shouldReturnBadRequest_whenInsufficientFunds() throws Exception {
        TransactionRequestDto requestDto = new TransactionRequestDto();
        requestDto.setAmount(new BigDecimal("1000000000"));
        requestDto.setSenderAccountNumber(new BigInteger("1234567890123"));
        requestDto.setRecipientAccountNumber(new BigInteger("1000000000012"));
        requestDto.setSenderIban("IR-0000727379956");
        requestDto.setType("WITHDRAWAL");
        requestDto.setDescription("Withdrawal from wallet");

        when(transactionService.withdraw(any(TransactionRequestDto.class)))
                .thenThrow(new InsufficientFundsException("Insufficient funds"));

        mockMvc.perform(post("/api/v1/transaction/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Insufficient funds"));
    }

    @Test
    void transfer_shouldReturnSuccessMessage() throws Exception {
        TransactionRequestDto requestDto = new TransactionRequestDto();
        requestDto.setAmount(new BigDecimal("100000"));
        requestDto.setSenderAccountNumber(new BigInteger("1234567890123"));
        requestDto.setRecipientAccountNumber(new BigInteger("1000000000012"));
        requestDto.setSenderIban("IR-0000727379956");
        requestDto.setType("TRANSFER");
        requestDto.setDescription("Transfer between wallets");

        when(transactionService.transfer(any(TransactionRequestDto.class)))
                .thenReturn("Transfer successful");

        mockMvc.perform(post("/api/v1/transaction/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Transfer successful"));
    }

    @Test
    void transfer_shouldReturnBadRequest_whenInsufficientFunds() throws Exception {
        TransactionRequestDto requestDto = new TransactionRequestDto();
        requestDto.setAmount(new BigDecimal("1000000000"));
        requestDto.setSenderAccountNumber(new BigInteger("1234567890123"));
        requestDto.setRecipientAccountNumber(new BigInteger("1000000000012"));
        requestDto.setSenderIban("IR-0000727379956");
        requestDto.setType("TRANSFER");
        requestDto.setDescription("Transfer between wallets");

        when(transactionService.transfer(any(TransactionRequestDto.class)))
                .thenThrow(new InsufficientFundsException("Insufficient funds for transfer"));

        mockMvc.perform(post("/api/v1/transaction/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Insufficient funds for transfer"));
    }
}