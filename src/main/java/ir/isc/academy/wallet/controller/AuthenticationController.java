package ir.isc.academy.wallet.controller;

import ir.isc.academy.wallet.dto.ResponseMessage;
import ir.isc.academy.wallet.dto.requestDto.UserLoginRequestDto;
import ir.isc.academy.wallet.dto.requestDto.UserRegisterRequestDto;
import ir.isc.academy.wallet.dto.responseDto.LoginResponseDTO;
import ir.isc.academy.wallet.exception.InvalidCredentialsException;
import ir.isc.academy.wallet.exception.UsernameAlreadyTakenException;
import ir.isc.academy.wallet.model.User;
import ir.isc.academy.wallet.service.AuthenticationService;
import ir.isc.academy.wallet.service.JwtService;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<ResponseMessage> register(@Valid @RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        ResponseMessage responseMessage = authenticationService.createUser(userRegisterRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@Valid @RequestBody UserLoginRequestDto loginUserDto) {
        try {
            User authenticatedUser = authenticationService.authenticate(loginUserDto);
            String jwtToken = jwtService.generateToken(authenticatedUser);
            LoginResponseDTO loginResponse = new LoginResponseDTO(jwtToken, jwtService.getExpirationTime());
            return ResponseEntity.ok(loginResponse);
        } catch (InvalidCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessage(ex.getMessage()));
        }
    }

    @ExceptionHandler(UsernameAlreadyTakenException.class)
    public ResponseEntity<ResponseMessage> handleUsernameAlreadyTaken(UsernameAlreadyTakenException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getResponseMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseMessage> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessages = new StringBuilder("Validation failed: ");
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errorMessages.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ")
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseMessage(errorMessages.toString()));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ResponseMessage> handleInvalidCredentials(InvalidCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessage(e.getMessage()));
    }

}
