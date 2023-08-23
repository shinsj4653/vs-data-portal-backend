package visang.dataplatform.dataportal.exception.notfound;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import visang.dataplatform.dataportal.exception.DataportalException;

@Getter
public class NotFoundException extends DataportalException {
    public NotFoundException(String message, int code) {
        super(HttpStatus.NOT_FOUND, message, code);
    }
}
