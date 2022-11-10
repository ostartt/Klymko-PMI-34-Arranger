package com.arranger.eurekaclient.exception.handler;

import com.arranger.eurekaclient.dto.UserDTO;
import com.arranger.eurekaclient.entity.User;
import com.arranger.eurekaclient.exception.EmailAlreadyTakenException;
import com.arranger.eurekaclient.exception.EmailNotConfirmedException;
import com.arranger.eurekaclient.exception.ResourcesExhaustedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(EmailAlreadyTakenException.class)
    protected ResponseEntity<Object> handleEmailAlreadyTaken(
            EmailAlreadyTakenException ex) {

        ApiError apiError = new ApiError(HttpStatus.NOT_ACCEPTABLE);
        apiError.setMessage(ex.getMessage());
        apiError.setDebugMessage(ex.toString());

        UserDTO userDTO = ex.getUserDTO();
        ApiValidationError apiValidationError = new ApiValidationError(List.of(userDTO), ex.getMessage());
        apiValidationError.setField("email");
        apiValidationError.setRejectedValue(userDTO.getEmail());

        apiError.setSubErrors(List.of(apiValidationError));

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(EmailNotConfirmedException.class)
    protected ResponseEntity<Object> handleEmailNotConfirmed(
            EmailNotConfirmedException ex) {

        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN);
        apiError.setMessage(ex.getMessage());
        apiError.setDebugMessage(ex.toString());

        User user = ex.getUser();
        ApiValidationError apiValidationError = new ApiValidationError(List.of(user), ex.getMessage());
        apiValidationError.setField("email");
        apiValidationError.setRejectedValue(user.getEmail());

        apiError.setSubErrors(List.of(apiValidationError));

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ResourcesExhaustedException.class)
    protected ResponseEntity<Object> resourcesExhausted(
            ResourcesExhaustedException ex) {

        ApiError apiError = new ApiError(HttpStatus.PAYLOAD_TOO_LARGE);
        apiError.setMessage(ex.getMessage());
        apiError.setDebugMessage(ex.toString());

        return buildResponseEntity(apiError);
    }

}
