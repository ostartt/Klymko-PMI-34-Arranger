package com.arranger.eurekaclient.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PermutationValidator implements ConstraintValidator<PermutationConstraint, String> {

    @Override
    public void initialize(PermutationConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }


    @Override
    public boolean isValid(String nameField, ConstraintValidatorContext constraintValidatorContext) {
        return nameField.matches("^.{3,10}$");
    }
}
