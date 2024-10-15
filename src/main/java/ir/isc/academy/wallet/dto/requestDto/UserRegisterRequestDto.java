package ir.isc.academy.wallet.dto.requestDto;

import ir.isc.academy.wallet.model.Gender;
import ir.isc.academy.wallet.validation.userValidation.Email;
import ir.isc.academy.wallet.validation.userValidation.IDNumber;
import ir.isc.academy.wallet.validation.userValidation.PhoneNumber;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserRegisterRequestDto {

    private UUID id;
    @IDNumber
    private String idNumber;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    @Email
    private String email;
    @PhoneNumber
    private String phoneNumber;
    private Gender gender;
    private Boolean militaryStatus;
    private LocalDate dateOfBirth;
    private Timestamp createdAt;
    @DecimalMin(value = "10000")
    private BigDecimal deposit;
    private BigInteger depositAccountNumber;

}
