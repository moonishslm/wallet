package ir.isc.academy.wallet.dto.responseDto;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TransactionResponseDTO {
    private UUID id;
    private BigDecimal amount;
    private String type; // "deposit", "withdraw", "transfer"
    private UUID sender;
    private UUID recipient;
    private String description;
    private Timestamp createdAt;
}
