package ir.isc.academy.wallet.dto.requestDto;
import ir.isc.academy.wallet.validation.walletValidation.AccountNumber;
import ir.isc.academy.wallet.validation.walletValidation.IBAN;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TransactionRequestDto {

    @NotNull(message = "Amount is mandatory")
    @DecimalMin(value = "100000", message = "Minimum amount is 0.01")
    private BigDecimal amount;

    @AccountNumber
    @NotNull(message = "Sender account number is mandatory")
    private BigInteger senderAccountNumber;

    @AccountNumber
    @NotNull(message = "Recipient account number is mandatory")
    private BigInteger recipientAccountNumber;

    @IBAN
    private String senderIban;

    @NotBlank(message = "Transaction type is mandatory")
    private String type; // "deposit", "withdrawal", "transfer"

    private String description;


}
