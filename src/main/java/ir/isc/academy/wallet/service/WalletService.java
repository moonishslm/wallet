package ir.isc.academy.wallet.service;

import ir.isc.academy.wallet.dto.responseDto.WalletResponseDTO;
import ir.isc.academy.wallet.model.User;
import ir.isc.academy.wallet.model.Wallet;
import ir.isc.academy.wallet.repository.UserRepo;
import ir.isc.academy.wallet.repository.WalletRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class WalletService {

    final private WalletRepo walletRepo;
    final private UserRepo userRepo;

    @Transactional
    public WalletResponseDTO readWallet(String username) {

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        Wallet wallet = walletRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found for user: " + username));

        return mapWalletToResponseDto(wallet);

    }

    private WalletResponseDTO mapWalletToResponseDto(Wallet wallet) {
        WalletResponseDTO responseDto = new WalletResponseDTO();
        responseDto.setWalletId(wallet.getId());
        responseDto.setUsername(wallet.getUser().getUsername());
        responseDto.setAccountNumber(wallet.getAccountNumber());
        responseDto.setBalance(wallet.getBalance());
        responseDto.setIban(wallet.getIban());
        responseDto.setCreatedAt(wallet.getCreatedAt());
        return responseDto;
    }
}
