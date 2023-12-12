package visang.dataplatform.dataportal.model.request.metadata;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Schema(description = "메타 데이터 페이지에서 검색 시, 검색어 자동완성 결과 API 요청 Body")
public class AutoCompleteSearchRequest {

    @Schema(description = "ES Index명")
    private String index;

    @Schema(description = "검색 기준 리스트")
    private List<String> search_conditions;

    @Schema(description = "검색 키워드")
    private String keyword;
}
