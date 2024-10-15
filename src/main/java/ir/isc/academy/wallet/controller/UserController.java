package ir.isc.academy.wallet.controller;

import ir.isc.academy.wallet.dto.ResponseMessage;
import ir.isc.academy.wallet.dto.requestDto.TransactionHistoryDto;
import ir.isc.academy.wallet.dto.requestDto.UserRegisterRequestDto;
import ir.isc.academy.wallet.dto.requestDto.UserUpdateRequestDto;
import ir.isc.academy.wallet.dto.responseDto.UserResponseDTO;
import ir.isc.academy.wallet.exception.UsernameAlreadyTakenException;
import ir.isc.academy.wallet.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    final UserService userService;

    @GetMapping("/read")
    public ResponseEntity<UserResponseDTO> readUser(@RequestParam String username) {
        UserResponseDTO userResponseDto = userService.readUser(username);
        return ResponseEntity.ok(userResponseDto);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseMessage> updateUser(@Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        ResponseMessage responseMessage = userService.updateUser(userUpdateRequestDto);
        return ResponseEntity.ok(responseMessage);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseMessage> deleteUser(@RequestParam String username) {
        ResponseMessage responseMessage = userService.deleteUser(username);
        return ResponseEntity.ok(responseMessage);
    }

    @GetMapping("/history")
    public ResponseEntity<List<TransactionHistoryDto>> userHistory(@RequestParam String username) {
        List<TransactionHistoryDto> transactionHistory = userService.userTransactionHistory(username);
        return ResponseEntity.ok(transactionHistory);
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
}


