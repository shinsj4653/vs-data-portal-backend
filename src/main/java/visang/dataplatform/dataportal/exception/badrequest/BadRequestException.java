package visang.dataplatform.dataportal.exception.badrequest;

import org.springframework.http.HttpStatus;
import visang.dataplatform.dataportal.exception.DataportalException;

public class BadRequestException extends DataportalException {
    public BadRequestException(String message, int code) {
        super(HttpStatus.BAD_REQUEST, message, code);
    }
}
