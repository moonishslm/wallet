package ir.isc.academy.wallet.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionHistoryDto {
    private UUID transactionId;
    private BigDecimal amount;
    private String type;
    private String sender;
    private String recipient;
    private String description;
    private Timestamp createdAt;
}
