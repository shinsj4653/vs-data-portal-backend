package visang.dataplatform.dataportal.model.dto.metadata;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "메타데이터 - 메타테이블 컬럼 정보 DTO")
public class TableColumnDto {

    @Schema(description = "컬럼 ID")
    private String table_col_id;

    @Schema(description = "컬럼명 (한글명)")
    private String table_col_name;
    
    @Schema(description = "데이터 타입")
    private String table_col_datatype;

    @Schema(description = "컬럼 설명")
    private String table_col_comment;
}
