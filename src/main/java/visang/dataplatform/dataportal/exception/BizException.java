package visang.dataplatform.dataportal.exception;

public class BizException extends RuntimeException {
    private int status;
    private String code;
    private String message;

    public BizException(BizExceptionCode bizExceptionCode) {
        this.status = bizExceptionCode.getStatus();
        this.code = bizExceptionCode.getCode();
        this.message = bizExceptionCode.getMessage();
    }
}
