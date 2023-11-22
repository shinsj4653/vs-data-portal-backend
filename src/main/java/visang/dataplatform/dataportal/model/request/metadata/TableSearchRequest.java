package visang.dataplatform.dataportal.model.request.metadata;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Schema(description = "메타 테이블 정보 화면에서, 테이블 ID 값 기준 검색 결과 조회 API 요청 Body")
public class TableSearchRequest {

    @NotEmpty
    @Schema(description = "검색 조건")
    private String search_condition;

    @NotNull
    @Schema(description = "검색 키워드")
    private String keyword;

    @Positive
    @Schema(description = "페이지 번호")
    private Integer page_no;

    @Positive
    @Schema(description = "페이지 당 갯수")
    private Integer amount_per_page;

}
