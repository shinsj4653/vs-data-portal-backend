package visang.dataplatform.dataportal.model.request.metadata;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "메타 데이터 정보내의 서비스와 대분류 정보에 해당하는 중분류 데이터 셋 API 요청 Body")
public class SubDatasetRequest {

    @Schema(description = "서비스 명")
    private String service_name;

    @Schema(description = "대분류 명")
    private String main_category_name;
}