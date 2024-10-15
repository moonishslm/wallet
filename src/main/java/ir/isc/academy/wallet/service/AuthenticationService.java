package ir.isc.academy.wallet.service;

import ir.isc.academy.wallet.dto.ResponseMessage;
import ir.isc.academy.wallet.dto.requestDto.UserLoginRequestDto;
import ir.isc.academy.wallet.dto.requestDto.UserRegisterRequestDto;
import ir.isc.academy.wallet.exception.InvalidCredentialsException;
import ir.isc.academy.wallet.exception.UsernameAlreadyTakenException;
import ir.isc.academy.wallet.model.Gender;
import ir.isc.academy.wallet.model.Transaction;
import ir.isc.academy.wallet.model.User;
import ir.isc.academy.wallet.model.Wallet;
import ir.isc.academy.wallet.repository.TransactionRepo;
import ir.isc.academy.wallet.repository.UserRepo;
import ir.isc.academy.wallet.repository.WalletRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final WalletRepo walletRepo;
    private final TransactionRepo transactionRepo;

    @PersistenceContext
    private EntityManager entityManager;

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

    public User authenticate(UserLoginRequestDto input) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getUsername(),
                            input.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid Username or password");
        }

        return userRepo.findByUsername(input.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));
    }

    private void validateMilitaryStatus(UserRegisterRequestDto registerRequestDto) {
        LocalDate birthDate = registerRequestDto.getDateOfBirth();
        int age = Period.between(birthDate, LocalDate.now()).getYears();

        if(registerRequestDto.getGender() == Gender.FEMALE || age < 18) {
            registerRequestDto.setMilitaryStatus(null);
        } else if (registerRequestDto.getGender() == Gender.MALE && registerRequestDto.getMilitaryStatus() == null) {
            throw new IllegalArgumentException("Military status must be provided for males 18 and older");
        }
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
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));

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
}
