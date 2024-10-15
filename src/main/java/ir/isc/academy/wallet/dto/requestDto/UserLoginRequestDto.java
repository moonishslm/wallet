package ir.isc.academy.wallet.dto.requestDto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserLoginRequestDto {
    private String username;
    private String password;
}
