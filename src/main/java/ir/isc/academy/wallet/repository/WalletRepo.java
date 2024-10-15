package ir.isc.academy.wallet.repository;

import ir.isc.academy.wallet.model.User;
import ir.isc.academy.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.*;

@Repository
public interface WalletRepo extends JpaRepository<Wallet, UUID> {

    Optional<Wallet> findByUserId(UUID userId);
    Optional<Wallet> findByAccountNumber(BigInteger accountNumber);
    Optional<Wallet> findByUser(User user);

}
