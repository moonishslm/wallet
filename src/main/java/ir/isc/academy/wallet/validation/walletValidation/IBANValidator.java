package ir.isc.academy.wallet.validation.walletValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IBANValidator implements ConstraintValidator<IBAN, String> {
    @Override
    public boolean isValid(String iban, ConstraintValidatorContext context) {
        return iban != null && iban.matches("IR-\\d{13}");
    }
}