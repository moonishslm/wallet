package ir.isc.academy.wallet.validation.walletValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigInteger;

public class AccountNumberValidator implements ConstraintValidator<AccountNumber, BigInteger> {
    @Override
    public boolean isValid(BigInteger value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        String accountNumber = value.toString();
        return accountNumber.length() == 13;
    }
}