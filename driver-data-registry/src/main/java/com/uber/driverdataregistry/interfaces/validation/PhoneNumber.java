package com.uber.driverdataregistry.interfaces.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * This @interface declaration defines a custom @PhoneNumber annotation which
 * can be used to validate phone numbers in a request DTO.
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface PhoneNumber {
	String message() default "must be a valid Indian phone number (e.g, +91 980 341 3311)";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
