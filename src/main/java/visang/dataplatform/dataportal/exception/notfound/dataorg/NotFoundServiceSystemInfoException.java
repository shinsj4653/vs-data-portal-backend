package visang.dataplatform.dataportal.exception.notfound.dataorg;

import org.springframework.http.HttpStatus;
import visang.dataplatform.dataportal.exception.DataportalException;
import visang.dataplatform.dataportal.exception.notfound.NotFoundException;

public class NotFoundServiceSystemInfoException extends NotFoundException {

    public NotFoundServiceSystemInfoException() {
        super("서비스명을 다시 한번 확인해주시길 바랍니다.", 1001);
    }
}
