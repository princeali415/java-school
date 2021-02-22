package com.lambdaschool.schools.handlers;

import com.lambdaschool.schools.models.ValidationError;

import java.util.List;

public interface HelperFunctions
{
    List<ValidationError> getConstraintViolation(Throwable cause);
}
