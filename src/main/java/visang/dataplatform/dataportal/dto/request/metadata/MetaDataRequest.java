package visang.dataplatform.dataportal.dto.request.metadata;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

@Getter
@Schema(description = "메타 데이터 정보 API 요청 Body")
public class MetaDataRequest {

    @Schema(description = "서비스 명")
    private String service_name;

    @Schema(description = "대분류 명")
    private String main_category_name;

    @Schema(description = "중분류 명")
    private String sub_category_name;
}
