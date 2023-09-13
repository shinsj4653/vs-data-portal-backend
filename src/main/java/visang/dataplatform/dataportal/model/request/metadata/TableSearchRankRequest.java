package visang.dataplatform.dataportal.model.request.metadata;

import io.swagger.v3.oas.annotations.media.Schema;
import jdk.jfr.Description;
import lombok.Getter;

@Getter
@Schema(description = "메타 테이블 검색어 실시간 순위 집계 API 요청 Body")
public class TableSearchRankRequest {

    // 검색 API 경로
    private String uri;

    // 검색 시간대 시작시간
    private String gte;

    // 검색 시간대 끝 시간
    private String lte;
}
