package visang.dataplatform.dataportal.model.dto.dataorg;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "매니저 정보 DTO")
public class ServiceManagerDto {
    @Schema(description = "서비스 담당자 역할")
    private String service_mngr_tkcg;
    @Schema(description = "서비스 담당자 부서")
    private String service_mngr_dept;
    @Schema(description = "서비스 담당자 이름")
    private String service_mngr_name;
}
