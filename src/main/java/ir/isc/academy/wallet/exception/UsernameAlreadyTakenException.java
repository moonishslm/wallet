package ir.isc.academy.wallet.exception;

import ir.isc.academy.wallet.dto.ResponseMessage;
import lombok.Getter;

@Getter
public class UsernameAlreadyTakenException extends RuntimeException {

    private final ResponseMessage responseMessage;

    public UsernameAlreadyTakenException() {
        this.responseMessage = new ResponseMessage("Username is already taken");
    }
}