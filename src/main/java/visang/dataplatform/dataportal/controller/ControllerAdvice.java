package visang.dataplatform.dataportal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import visang.dataplatform.dataportal.exception.DataportalException;
import visang.dataplatform.dataportal.model.response.error.ErrorResponse;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {
    private static final int FIELD_ERROR_CODE_INDEX = 0;
    private static final int FIELD_ERROR_MESSAGE_INDEX = 1;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleInputFieldException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors()
                .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorResponse> sqlExceptionHandle(DataportalException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(new ErrorResponse(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(DataportalException.class)
    public ResponseEntity<ErrorResponse> handleDataportalException(DataportalException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(new ErrorResponse(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> unhandledException(Exception e, HttpServletRequest request) {
        log.error("UnhandledException: {} {} errMessage={}\n",
                request.getMethod(),
                request.getRequestURI(),
                e.getMessage()
        );
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(9999, "일시적으로 접속이 원활하지 않습니다. 데이터 플랫폼 Cell 팀으로 문의 부탁드립니다."));
    }
}
