import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ir.isc.academy.wallet.controller.AuthenticationController;
import ir.isc.academy.wallet.dto.ResponseMessage;
import ir.isc.academy.wallet.dto.requestDto.UserLoginRequestDto;
import ir.isc.academy.wallet.dto.requestDto.UserRegisterRequestDto;
import ir.isc.academy.wallet.exception.InvalidCredentialsException;
import ir.isc.academy.wallet.exception.UsernameAlreadyTakenException;
import ir.isc.academy.wallet.model.Gender;
import ir.isc.academy.wallet.model.User;
import ir.isc.academy.wallet.service.AuthenticationService;
import ir.isc.academy.wallet.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
class AuthenticationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationService authenticationService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(
                new AuthenticationController(jwtService, authenticationService)
        ).build();

        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void register_shouldReturnCreatedStatus() throws Exception {
        UserRegisterRequestDto registerDto = new UserRegisterRequestDto();
        registerDto.setIdNumber("0058874598");
        registerDto.setUsername("testuser");
        registerDto.setPassword("password123");
        registerDto.setFirstName("mahshad");
        registerDto.setLastName("salimi");
        registerDto.setEmail("salimi@example.com");
        registerDto.setPhoneNumber("09123456789");
        registerDto.setGender(Gender.MALE);
        registerDto.setMilitaryStatus(true);
        registerDto.setDateOfBirth(LocalDate.of(1990, 11, 27));
        registerDto.setDeposit(new BigDecimal("10000"));
        registerDto.setDepositAccountNumber(new BigInteger("1000000000"));

        when(authenticationService.createUser(any(UserRegisterRequestDto.class)))
                .thenReturn(new ResponseMessage("User is created successfully"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User is created successfully"));
    }

    @Test
    void register_shouldReturnConflictStatus_whenUsernameAlreadyTaken() throws Exception {
        UserRegisterRequestDto registerDto = new UserRegisterRequestDto();
        registerDto.setIdNumber("1234567890");
        registerDto.setUsername("testuser");
        registerDto.setPassword("password123");
        registerDto.setFirstName("Jane");
        registerDto.setLastName("Doe");
        registerDto.setEmail("jane.doe@example.com");
        registerDto.setPhoneNumber("09187654321");
        registerDto.setGender(Gender.FEMALE);
        registerDto.setMilitaryStatus(false);
        registerDto.setDateOfBirth(LocalDate.of(1995, 5, 5));
        registerDto.setDeposit(new BigDecimal("20000"));
        registerDto.setDepositAccountNumber(new BigInteger("2000000000"));

        when(authenticationService.createUser(any(UserRegisterRequestDto.class)))
                .thenThrow(new UsernameAlreadyTakenException());

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isConflict());
    }

    @Test
    void login_shouldReturnOkStatus() throws Exception {
        UserLoginRequestDto loginDto = new UserLoginRequestDto();
        loginDto.setUsername("testuser");
        loginDto.setPassword("password123");

        User authenticatedUser = new User();
        authenticatedUser.setUsername("testuser");

        when(authenticationService.authenticate(any(UserLoginRequestDto.class)))
                .thenReturn(authenticatedUser);
        when(jwtService.generateToken(any(User.class)))
                .thenReturn("testJwtToken");
        when(jwtService.getExpirationTime())
                .thenReturn(3600L);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("testJwtToken"))
                .andExpect(jsonPath("$.expiresIn").value(3600));
    }

    @Test
    void login_shouldReturnUnauthorizedStatus_whenInvalidCredentials() throws Exception {
        UserLoginRequestDto loginDto = new UserLoginRequestDto();
        loginDto.setUsername("testuser");
        loginDto.setPassword("wrongpassword");

        when(authenticationService.authenticate(any(UserLoginRequestDto.class)))
                .thenThrow(new InvalidCredentialsException("Invalid username or password"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid username or password"));
    }
}