package visang.dataplatform.dataportal.model.dto.dataorg;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "데이터 조직도 - 서비스 시스템 정보 DTO")
public class ServiceSystemInfoDto {
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
    @Schema(description = "서비스 매니저 정보 리스트")
    private List<ServiceManagerDto> manager_list;
}
