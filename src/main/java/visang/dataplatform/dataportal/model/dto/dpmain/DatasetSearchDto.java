package visang.dataplatform.dataportal.model.dto.dpmain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
@Schema(description = "데이터 포털 메인 - 데이터 셋 검색 결과 DTO")
public class DatasetSearchDto {
    @Schema(description = "서비스 명")
    private String service_name;
    
    @Schema(description = "대분류 주제영역")
    private String main_category_name;

    @Schema(description = "중분류 주제영역")
    private String sub_category_name;

    @Schema(description = "검색 결과 총 갯수")
    private long total_num;
}
