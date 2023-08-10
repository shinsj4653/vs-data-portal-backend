package visang.dataplatform.dataportal.model.dto.dpmain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.ibatis.type.Alias;

@Getter
@AllArgsConstructor
@Schema(description = "데이터 포털 메인 - 데이터 셋 검색 결과 DTO")
public class DatasetSearchDto {
    @Schema(description = "서비스 명")
    private String service_name;
    
    @Schema(description = "데이터 셋 명")
    private String dataset_name;
}
