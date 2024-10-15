package ir.isc.academy.wallet.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "national_idnumber", nullable = false ,unique=true)
    private String idNumber;

    @Column(name = "username", nullable = false)
    @NotBlank(message = "Username is mandatory")
    private String username;

    @Column(name = "password", length = 100, nullable = true)
    private String password;

    @Column(name = "firstname", nullable = false)
    @NotBlank(message = "Firstname is mandatory")
    private String firstName;

    @Column(name = "lastname", nullable = false)
    @NotBlank(message = "Lastname is mandatory")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "military_status")
    private Boolean militaryStatus;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "created_date")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "daily_transaction_total")
    private BigDecimal dailyTransactionTotal = BigDecimal.ZERO;

    @Column(name = "last_transaction_date")
    private LocalDate lastTransactionDate;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Wallet wallet;

    // for roles
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
