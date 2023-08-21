package visang.dataplatform.dataportal.model.request.dataorg;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "데이터 조직도에서 대분류 데이터셋 기준 서비스명 조회 API 요청 Body")
public class SearchByMainDatasetRequest {

    @Schema(description = "검색 대분류 셋 명")
    private String mainDataset;
}
