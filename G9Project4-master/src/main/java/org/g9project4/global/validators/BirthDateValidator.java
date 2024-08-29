package org.g9project4.global.validators;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class BirthDateValidator implements ConstraintValidator<ValidBirthDate, LocalDate> {

    private int minAge;

    @Override
    public void initialize(ValidBirthDate constraintAnnotation) {
        this.minAge = constraintAnnotation.minAge();
    }

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        if (birthDate == null) {
            return false; // null 값은 유효하지 않음
        }

        // 현재 날짜보다 이후의 날짜인 경우 유효하지 않음
        if (birthDate.isAfter(LocalDate.now())) {
            return false;
        }

        // 최소 나이가 설정된 경우, 해당 나이 이상인지 확인
        if (minAge > 0) {
            return Period.between(birthDate, LocalDate.now()).getYears() >= minAge;
        }

        return true;
    }
}
