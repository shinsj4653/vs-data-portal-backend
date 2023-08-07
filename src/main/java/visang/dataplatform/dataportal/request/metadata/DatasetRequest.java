package visang.dataplatform.dataportal.request.metadata;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "메타 데이터 정보내의 서비스에 해당하는 데이터 셋 API 요청 Body")
public class DatasetRequest {

    @Schema(description = "서비스 명")
    private String service_name;
}
