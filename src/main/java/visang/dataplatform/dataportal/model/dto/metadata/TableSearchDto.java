package visang.dataplatform.dataportal.model.dto.metadata;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "메타데이터 - 테이블 검색 결과 DTO")
public class TableSearchDto {

    @Schema(description = "테이블 ID")
    private String table_id;

    @Schema(description = "테이블명")
    private String table_name;

    @Schema(description = "테이블 설명")
    private String table_comment;

    @Schema(description = "테이블 소주제명")
    private String small_clsf_name;
}
