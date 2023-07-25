package visang.dataplatform.dataportal.dto.response.dataorg;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "데이터 조직도 - 서비스 시스템 정보 DTO")
public class DataOrgSystemInfoDto {
    @Schema(description = "컴퍼니 명")
    private String company_name;
    @Schema(description = "서비스 명")
    private String service_name;
    @Schema(description = "서비스 웹 사이트 url 주소")
    private String service_web_url;
    @Schema(description = "서비스 OS 정보")
    private String service_os;
    @Schema(description = "서비스 WAS 정보")
    private String service_was;
    @Schema(description = "서비스 DB 정보")
    private String service_db;
}
