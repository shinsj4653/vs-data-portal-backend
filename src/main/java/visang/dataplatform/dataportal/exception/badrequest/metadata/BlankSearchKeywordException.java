package visang.dataplatform.dataportal.exception.badrequest.metadata;

import visang.dataplatform.dataportal.exception.badrequest.BadRequestException;

public class BlankSearchKeywordException extends BadRequestException {
    public BlankSearchKeywordException() {
        super("검색어를 입력하세요.", 2001);
    }
}
