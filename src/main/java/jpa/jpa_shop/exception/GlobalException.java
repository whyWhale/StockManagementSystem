package jpa.jpa_shop.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = NotEnoughStockException.class)
    public ResponseEntity<ErrorResponse> NotEnoughStockException(RuntimeException exception)
    {
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.EXPECTATION_FAILED, "재고를 확인해주세요.");
        return new ResponseEntity<>(errorResponse,HttpStatus.EXPECTATION_FAILED);
    }

    // presentationLayer validation Error
    @ExceptionHandler(value= MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> ValidationError(MethodArgumentNotValidException exception)
    {
        String message = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        ErrorResponse errorResponse=ErrorResponse.of(HttpStatus.NOT_ACCEPTABLE, message);
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = IllegalStateException.class)
    public ResponseEntity<ErrorResponse> illegalStateException(IllegalStateException exception)
    {
        String message = exception.getMessage();
        ErrorResponse errorResponse=ErrorResponse.of(HttpStatus.CONFLICT,message);
        return new ResponseEntity<>(errorResponse,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> Nodata(IllegalArgumentException exception)
    {
        String message = exception.getMessage();
        ErrorResponse errorResponse=ErrorResponse.of(HttpStatus.NO_CONTENT,message);
        return new ResponseEntity<>(errorResponse,HttpStatus.NO_CONTENT);
    }
}
