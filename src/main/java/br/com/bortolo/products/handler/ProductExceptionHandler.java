package br.com.bortolo.products.handler;


import br.com.bortolo.products.exceptions.ProductException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@ControllerAdvice
public class ProductExceptionHandler {

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<List<String>> handleProductException(ProductException ex) {
        List<String> errors = new ArrayList<>();
            errors.add(ex.getMessage());
            errors.add(Arrays.toString(ex.getStackTrace()));
        Collections.sort(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
