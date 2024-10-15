package ir.isc.academy.wallet.validation.userValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IDNumberValidator implements ConstraintValidator<IDNumber, String> {

    @Override
    public boolean isValid(String idNumber, ConstraintValidatorContext context) {
        return idNumber != null && idNumber.matches("\\d{10}");
    }
}