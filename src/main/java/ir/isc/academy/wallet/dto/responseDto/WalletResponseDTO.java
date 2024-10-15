package ir.isc.academy.wallet.dto.responseDto;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class WalletResponseDTO {
    private UUID walletId;
    private String username;
    private BigInteger accountNumber;
    private BigDecimal balance;
    private String iban;
    private Timestamp createdAt;
}
