package visang.dataplatform.dataportal.request.dataorg;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "데이터 조직도에서 타켓 기준 서비스 명 조회 API 요청 Body")
public class ServiceTargetRequest {

    @Schema(description = "서비스 타켓 명")
    private String target_name;
}
