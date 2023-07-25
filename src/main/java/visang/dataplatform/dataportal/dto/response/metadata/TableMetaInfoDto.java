package visang.dataplatform.dataportal.dto.response.metadata;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "메타데이터 - 테이블 메타 정보 DTO")
public class TableMetaInfoDto {

    @Schema(description = "테이블 메타 정보의 아이디(인덱스)")
    private String table_meta_info_id;

    @Schema(description = "테이블 아이디명")
    private String table_id;

    @Schema(description = "테이블명")
    private String table_name;

    @Schema(description = "테이블 코멘트")
    private String table_comment;
}
