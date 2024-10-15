package ir.isc.academy.wallet.service;

import ir.isc.academy.wallet.dto.responseDto.WalletResponseDTO;
import ir.isc.academy.wallet.model.Wallet;
import ir.isc.academy.wallet.repository.WalletRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class WalletService {

    final private WalletRepo walletRepo;

    @Transactional
    public WalletResponseDTO readWallet(UUID userId) {

        Wallet wallet = walletRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for user ID: " + userId));
        return mapWalletToResponseDto(wallet);
    }

    private WalletResponseDTO mapWalletToResponseDto(Wallet wallet) {
        WalletResponseDTO responseDto = new WalletResponseDTO();
        responseDto.setWalletId(wallet.getId());
        responseDto.setUserId(wallet.getUser().getId());
        responseDto.setAccountNumber(wallet.getAccountNumber());
        responseDto.setBalance(wallet.getBalance());
        responseDto.setIban(wallet.getIban());
        responseDto.setCreatedAt(wallet.getCreatedAt());
        return responseDto;
    }
}
