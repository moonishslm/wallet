package ir.isc.academy.wallet.dto.requestDto;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class WalletRequestDto {
    private UUID id;
    private BigInteger accountNumber;
    private BigDecimal balance;
    private String iban;
    private String accountName;
}
