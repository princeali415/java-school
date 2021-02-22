package com.lambdaschool.schools.handlers;

import com.lambdaschool.schools.exceptions.ResourceNotFoundException;
import com.lambdaschool.schools.models.ErrorDetail;
import com.lambdaschool.schools.models.HelperFunctions;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler
{
    @Autowired
    private HelperFunctions helperFunctions;

    public RestExceptionHandler()
    {
        super();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException rnfe)
    {
        ErrorDetail errorDetail = new ErrorDetail();

        errorDetail.setTimestamp(new Date());
        errorDetail.setStatus((HttpStatus.NOT_FOUND.value()));
        errorDetail.setTitle("Resource Internal Exception");
        errorDetail.setDetail(rnfe.getMessage());
        errorDetail.setDeveloperMessage(rnfe.getClass().getName());
        errorDetail.setErrors((helperFunctions.getConstraintViolation(rnfe)));

        return new ResponseEntity<>(errorDetail, null, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
        Exception ex,
        Object body,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request)
    {
        ErrorDetail errorDetail = new ErrorDetail();

        errorDetail.setTimestamp(new Date());
        errorDetail.setStatus(status.value());
        errorDetail.setTitle("Rest Internal Exception");
        errorDetail.setDetail(ex.getMessage());
        errorDetail.setDeveloperMessage(ex.getClass().getName());
        errorDetail.setErrors((helperFunctions.getConstraintViolation(ex)));

        return new ResponseEntity<>(errorDetail, null, status);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
        NoHandlerFoundException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request)
    {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimestamp(new Date());
        errorDetail.setStatus(status.value());
        errorDetail.setTitle("Rest Internal Exception");
        errorDetail.setDetail("Found an issue with School: No handler found for GET /turtle");
        errorDetail.setDeveloperMessage("org.springframework.web.servlet.NoHandlerFoundException");
        errorDetail.setErrors(helperFunctions.getConstraintViolation(ex));

        return new ResponseEntity<>(errorDetail, null, status);
    }

    // /students/student/john error

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
        TypeMismatchException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request)
    {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimestamp(new Date());
        errorDetail.setStatus(status.value());
        errorDetail.setTitle("Rest Internal Exception");
        errorDetail.setDetail("Found an issue with student: "+ ex.getMessage());
        errorDetail.setDeveloperMessage(ex.getClass()
            .getName() + " " + ex.getMostSpecificCause());
        errorDetail.setErrors(helperFunctions.getConstraintViolation(ex));

        return new ResponseEntity<>(errorDetail,
            null,
            status);
    }
}
