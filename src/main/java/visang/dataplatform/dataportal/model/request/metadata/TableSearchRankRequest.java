package visang.dataplatform.dataportal.model.request.metadata;

import io.swagger.v3.oas.annotations.media.Schema;
import jdk.jfr.Description;
import lombok.Getter;

@Getter
@Schema(description = "메타 테이블 검색어 실시간 순위 집계 API 요청 Body")
public class TableSearchRankRequest {

    // 검색 API의 URI명
    private String requestURI;

    // 원하는 로그가 전송되는 API 종류
    private String logType;
}
