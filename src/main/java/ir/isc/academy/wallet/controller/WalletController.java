package ir.isc.academy.wallet.controller;

import ir.isc.academy.wallet.dto.responseDto.WalletResponseDTO;
import ir.isc.academy.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping(value = "/api/v1/wallet")
@RequiredArgsConstructor
public class WalletController {

    final WalletService walletService;

    @GetMapping("/read")
    public ResponseEntity<WalletResponseDTO> readWallet(@RequestParam UUID userId) {
        try {
            WalletResponseDTO walletResponseDTO = walletService.readWallet(userId);
            return ResponseEntity.ok(walletResponseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
    }
}
}
