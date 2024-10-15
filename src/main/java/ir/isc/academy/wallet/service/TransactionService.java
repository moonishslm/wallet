package ir.isc.academy.wallet.service;

import ir.isc.academy.wallet.dto.requestDto.TransactionRequestDto;
import ir.isc.academy.wallet.exception.InsufficientFundsException;
import ir.isc.academy.wallet.model.Transaction;
import ir.isc.academy.wallet.model.User;
import ir.isc.academy.wallet.model.Wallet;
import ir.isc.academy.wallet.repository.TransactionRepo;
import ir.isc.academy.wallet.repository.UserRepo;
import ir.isc.academy.wallet.repository.WalletRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class TransactionService {

    final TransactionRepo transactionRepo;
    final WalletRepo walletRepo;
    final UserRepo userRepo;

    private static final BigDecimal MINIMUM_BALANCE = BigDecimal.valueOf(10000);
    private static final BigDecimal DAILY_LIMIT = BigDecimal.valueOf(100000000);

    @Transactional
    public String deposit(TransactionRequestDto transactionRequest) {
        Wallet wallet = walletRepo.findByAccountNumber(transactionRequest.getRecipientAccountNumber())
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
        wallet.setBalance(wallet.getBalance().add(transactionRequest.getAmount()));

        Transaction transaction = createTransaction(wallet, transactionRequest.getAmount(),
                "DEPOSIT",transactionRequest.getSenderAccountNumber(),
                wallet.getAccountNumber(), transactionRequest.getDescription());
        transactionRepo.save(transaction);
        return "Deposit successful!";
    }

    @Transactional
    public String withdraw(TransactionRequestDto transactionRequest) {
        Wallet wallet = walletRepo.findByAccountNumber(transactionRequest.getSenderAccountNumber())
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        User user = wallet.getUser();

        checkAndUpdateDailyLimit(user, transactionRequest.getAmount());

        if (wallet.getBalance().subtract(transactionRequest.getAmount()).compareTo(MINIMUM_BALANCE) < 0) {
            throw new InsufficientFundsException("Insufficient Funds for Withdrawal");
        }
        wallet.setBalance(wallet.getBalance().subtract(transactionRequest.getAmount()));

        Transaction transaction = createTransaction(wallet, transactionRequest.getAmount(),
                "WITHDRAWAL", wallet.getAccountNumber(),
                transactionRequest.getRecipientAccountNumber(),
                transactionRequest.getDescription());
        transactionRepo.save(transaction);
        return "Withdrawal successful!";
    }

    @Transactional
    public String transfer(TransactionRequestDto transactionRequest) {
        Wallet senderWallet = walletRepo.
                findByAccountNumber(transactionRequest.getSenderAccountNumber())
                .orElseThrow(() -> new RuntimeException("Sender wallet not found"));

        User senderUser = senderWallet.getUser();

        Wallet recipientWallet = walletRepo.
                findByAccountNumber(transactionRequest.getRecipientAccountNumber())
                .orElseThrow(() -> new RuntimeException("Recipient wallet not found"));

        checkAndUpdateDailyLimit(senderUser, transactionRequest.getAmount());

        if (senderWallet.getBalance().subtract(transactionRequest.getAmount()).compareTo(MINIMUM_BALANCE) < 0) {
            throw new InsufficientFundsException("Insufficient Funds for Sender Wallet");
        }

        senderWallet.setBalance(senderWallet.getBalance().subtract(transactionRequest.getAmount()));
        recipientWallet.setBalance(recipientWallet.getBalance().add(transactionRequest.getAmount()));

        Transaction transaction = createTransaction(senderWallet,
                transactionRequest.getAmount(), "TRANSFER",
                senderWallet.getAccountNumber(), recipientWallet.getAccountNumber(),
                transactionRequest.getDescription());
        transactionRepo.save(transaction);

        return "Transfer successful!";
    }

    private Transaction createTransaction(Wallet wallet,
                                          BigDecimal amount, String type,
                                          BigInteger senderId, BigInteger recipientId,
                                          String description) {
        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setSender(senderId);
        transaction.setRecipient(recipientId);
        transaction.setDescription(description);

        return transaction;
    }

    private void checkAndUpdateDailyLimit(User user, BigDecimal amount) {
        LocalDate today = LocalDate.now();
        if (!today.equals(user.getLastTransactionDate())) {
            user.setDailyTransactionTotal(BigDecimal.ZERO);
            user.setLastTransactionDate(today);
        }

        BigDecimal newDailyTotal = user.getDailyTransactionTotal().add(amount);
        if (newDailyTotal.compareTo(DAILY_LIMIT) > 0) {
            throw new InsufficientFundsException("Daily transfer limit exceeded");
        }

        user.setDailyTransactionTotal(newDailyTotal);
        userRepo.save(user);

    }
}


