package visang.dataplatform.dataportal.dto.response.metadata;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "메타데이터 - 소분류 정보 DTO")
public class SubCategoryDto {

    @Schema(description = "소분류 값 id")
    private String sub_category_id;

    @Schema(description = "소분류 명")
    private String sub_category_name;
}
