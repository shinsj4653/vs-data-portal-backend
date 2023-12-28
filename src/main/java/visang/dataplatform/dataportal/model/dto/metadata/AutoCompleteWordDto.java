package visang.dataplatform.dataportal.model.dto.metadata;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "검색어 자동완성 결과 DTO")
public class AutoCompleteWordDto {

    @Schema(description = "자동완성된 키워드")
    private String keyword;

    @Schema(description = "검색 정확도")
    private float score;

}
