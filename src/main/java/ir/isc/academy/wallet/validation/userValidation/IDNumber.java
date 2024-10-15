package ir.isc.academy.wallet.validation.userValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = IDNumberValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface IDNumber {
    String message() default "Invalid ID number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}