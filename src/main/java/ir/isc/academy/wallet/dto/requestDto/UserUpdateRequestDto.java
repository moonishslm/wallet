package ir.isc.academy.wallet.dto.requestDto;

import lombok.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserUpdateRequestDto {
    private UUID id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private boolean militaryStatus;
    private LocalDate dateOfBirth;
}
