package ir.isc.academy.wallet.controller;

import ir.isc.academy.wallet.dto.requestDto.TransactionRequestDto;
import ir.isc.academy.wallet.exception.InsufficientFundsException;
import ir.isc.academy.wallet.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;

@RestController
@RequestMapping(value = "/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {

    final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@Valid @RequestBody TransactionRequestDto transactionRequest) {
        String response = transactionService.deposit(transactionRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdrawal(@Valid @RequestBody TransactionRequestDto transactionRequest) {
        try {
            String response = transactionService.withdraw(transactionRequest);
            return ResponseEntity.ok(response);
        } catch (InsufficientFundsException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@Valid @RequestBody TransactionRequestDto transactionRequest) {
        try {
            String response = transactionService.transfer(transactionRequest);
            return ResponseEntity.ok(response);
        } catch (InsufficientFundsException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    }
