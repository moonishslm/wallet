package ir.isc.academy.wallet.repository;

import ir.isc.academy.wallet.model.Transaction;
import ir.isc.academy.wallet.model.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigInteger;

import java.util.*;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, UUID> {

    @Query
    ("SELECT t FROM Transaction t WHERE t.wallet = :wallet OR t.sender = :accountNumber OR t.recipient = :accountNumber")
    List<Transaction> findByWalletOrAccountNumber(@Param("wallet") Wallet wallet, @Param("accountNumber") BigInteger accountNumber);
}
