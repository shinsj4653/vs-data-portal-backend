package visang.dataplatform.dataportal.request.metadata;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "메타 테이블 정보 화면에서, 테이블 ID 값 기준 검색 결과 조회 API 요청 Body")
public class TableSearchRequest {

    @Schema(description = "서비스 명")
    private String service_name;

    @Schema(description = "테이블 키워드")
    private String table_keyword;

    @Schema(description = "페이지 번호")
    private Integer page_no;

    @Schema(description = "페이지 당 갯수")
    private Integer amount_per_page;

}
