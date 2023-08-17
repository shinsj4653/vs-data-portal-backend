package visang.dataplatform.dataportal.model.request.dpmain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "데이터 셋을 포함하고 있는 서비스 조회 API 요청 Body")
public class DatasetSearchRequest {
    public String keyword;
}
