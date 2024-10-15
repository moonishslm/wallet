package ir.isc.academy.wallet.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.*;
import java.sql.*;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Column(name = "amount", precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(name = "type") //"deposit", "withdrawal" , "transaction"
    private String type;

    @Column(name = "sender_id", nullable = false)
    private BigInteger sender;

    @Column(name = "recipient_id", nullable = false)
    private BigInteger recipient;

    @Column(name = "description")
    private String description;

    @Column(name = "created_date")
    @CreationTimestamp
    private Timestamp createdAt;
}
