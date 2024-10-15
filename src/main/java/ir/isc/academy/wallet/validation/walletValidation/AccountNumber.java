package ir.isc.academy.wallet.validation.walletValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AccountNumberValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface AccountNumber {
    String message() default "Invalid account number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}