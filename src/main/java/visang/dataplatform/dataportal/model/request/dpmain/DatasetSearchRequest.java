package visang.dataplatform.dataportal.model.request.dpmain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.Positive;

@Getter
@Schema(description = "데이터 셋을 포함하고 있는 서비스 조회 API 요청 Body")
public class DatasetSearchRequest {
    @Schema(description = "검색 키워드")
    private String keyword;

    @Positive
    @Schema(description = "페이지 번호")
    private Integer page_no;

    @Positive
    @Schema(description = "페이지 당 갯수")
    private Integer amount_per_page;
}
