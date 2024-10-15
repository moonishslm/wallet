package ir.isc.academy.wallet.service;

import ir.isc.academy.wallet.dto.ResponseMessage;
import ir.isc.academy.wallet.dto.requestDto.TransactionHistoryDto;
import ir.isc.academy.wallet.dto.requestDto.UserRegisterRequestDto;
import ir.isc.academy.wallet.dto.requestDto.UserUpdateRequestDto;
import ir.isc.academy.wallet.dto.responseDto.UserResponseDTO;
import ir.isc.academy.wallet.exception.UsernameAlreadyTakenException;
import ir.isc.academy.wallet.model.Gender;
import ir.isc.academy.wallet.model.Transaction;
import ir.isc.academy.wallet.model.Wallet;
import ir.isc.academy.wallet.repository.TransactionRepo;
import ir.isc.academy.wallet.repository.UserRepo;
import ir.isc.academy.wallet.repository.WalletRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ir.isc.academy.wallet.model.User;
import org.springframework.stereotype.Service;

import java.math.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    final private UserRepo userRepo;
    final private WalletRepo walletRepo;
    final private TransactionRepo transactionRepo;
    private final JwtService jwtService;


    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public ResponseMessage createUser(UserRegisterRequestDto registerRequestDto) {
        validateMilitaryStatus(registerRequestDto);

        if (userRepo.findByUsername(registerRequestDto.getUsername()).isPresent()) {
            throw new UsernameAlreadyTakenException();
        }

        User user = mapDtoToUser(registerRequestDto);
        user = userRepo.save(user);

        Wallet wallet = generateWalletForUser(user, registerRequestDto.getDeposit());
        wallet = walletRepo.save(wallet);

        createInitialDepositTransaction(wallet, registerRequestDto);

        return new ResponseMessage("User is created successfully");
    }

    private void validateMilitaryStatus(UserRegisterRequestDto registerRequestDto) {
        LocalDate birthDate = registerRequestDto.getDateOfBirth();
        int age = Period.between(birthDate, LocalDate.now()).getYears();

        if(registerRequestDto.getGender() == Gender.FEMALE || age < 18) {
            registerRequestDto.setMilitaryStatus(null);
        } else if (registerRequestDto.getGender() == Gender.MALE && age >= 18 && registerRequestDto.getMilitaryStatus() == null) {
            throw new IllegalArgumentException("Military status must be provided for males 18 and older");
        }
    }
    private void createInitialDepositTransaction(Wallet wallet, UserRegisterRequestDto registerRequestDto) {
        Transaction transaction = new Transaction();
        transaction.setAmount(registerRequestDto.getDeposit());
        transaction.setDescription("initial deposit");
        transaction.setType("DEPOSIT");
        transaction.setSender(registerRequestDto.getDepositAccountNumber());
        transaction.setRecipient(wallet.getAccountNumber());
        transaction.setWallet(wallet);
        transactionRepo.save(transaction);
    }

    @Transactional
    public UserResponseDTO readUser(String username) {
        return userRepo.findByUsername(username).map(this::mapUserToResponseDto)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

    }

    @Transactional
    public ResponseMessage updateUser(UserUpdateRequestDto userUpdateRequestDto) {
        String username = userUpdateRequestDto.getUsername();
        User existingUser = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("UserName not found"));

        existingUser.setFirstName(userUpdateRequestDto.getFirstName());
        existingUser.setLastName(userUpdateRequestDto.getLastName());
        existingUser.setEmail(userUpdateRequestDto.getEmail());
        existingUser.setMilitaryStatus(userUpdateRequestDto.isMilitaryStatus());
        existingUser.setDateOfBirth(userUpdateRequestDto.getDateOfBirth());
        userRepo.save(existingUser);
        return new ResponseMessage("User updated successfully");
    }

    @Transactional
    public ResponseMessage deleteUser(String username) {
        User existingUser = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        userRepo.delete(existingUser);
        return new ResponseMessage("User deleted successfully");
    }

    @Transactional
    public List<TransactionHistoryDto> userTransactionHistory(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        Wallet wallet = walletRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found for user: " + username));

        List<Transaction> transactions = transactionRepo.findByWalletOrAccountNumber(wallet, wallet.getAccountNumber());

        return transactions.stream()
                .map(this::mapToTransactionHistoryDto)
                .collect(Collectors.toList());
    }

    private UserResponseDTO mapUserToResponseDto(User user) {
        UserResponseDTO userResponseDto = new UserResponseDTO();
        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setGender(user.getGender());
        userResponseDto.setDateOfBirth(user.getDateOfBirth());
        userResponseDto.setCreatedAt(user.getCreatedAt());
        return userResponseDto;
    }

    private TransactionHistoryDto mapToTransactionHistoryDto(Transaction transaction) {
        TransactionHistoryDto dto = new TransactionHistoryDto();
        dto.setTransactionId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setType(transaction.getType());
        dto.setSender(transaction.getSender().toString());
        dto.setRecipient(transaction.getRecipient().toString());
        dto.setDescription(transaction.getDescription());
        dto.setCreatedAt(transaction.getCreatedAt());

        return dto;
    }

    private User mapDtoToUser(UserRegisterRequestDto registerRequestDto) {
        User user = new User();
        user.setUsername(registerRequestDto.getUsername());
        user.setIdNumber(registerRequestDto.getIdNumber());
        user.setFirstName(registerRequestDto.getFirstName());
        user.setLastName(registerRequestDto.getLastName());
        user.setPhoneNumber(registerRequestDto.getPhoneNumber());
        user.setEmail(registerRequestDto.getEmail());
        user.setGender(registerRequestDto.getGender());
        user.setMilitaryStatus(registerRequestDto.getMilitaryStatus());
        user.setDateOfBirth(registerRequestDto.getDateOfBirth());

        return user;

    }

    private Wallet generateWalletForUser(User user, BigDecimal deposit) {
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalance(deposit);
        Integer iban = getNextIbanSequence();
        BigInteger accountNumber = getNextAccountSequence();
        wallet.setAccountNumber(accountNumber);
        wallet.setIban(generateIban(iban));
        return wallet;
    }

    private Integer getNextIbanSequence() {
        return ((Number) entityManager.createNativeQuery("SELECT nextval('iban_seq')").getSingleResult()).intValue();
    }

    private BigInteger getNextAccountSequence() {
        return new BigInteger(entityManager.createNativeQuery("SELECT nextval('account_number_seq')").getSingleResult().toString());
    }

    private String generateIban(Integer iban) {
        return "IR" + String.format("%014d", iban);
    }
}
