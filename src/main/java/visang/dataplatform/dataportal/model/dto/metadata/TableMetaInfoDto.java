package visang.dataplatform.dataportal.model.dto.metadata;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "메타데이터 - 테이블 메타 정보 DTO")
public class TableMetaInfoDto {

    @Schema(description = "테이블 메타 정보의 아이디(인덱스)")
    private String table_meta_info_id;

    @Schema(description = "테이블 아이디 명")
    private String table_id;

    @Schema(description = "테이블 코멘트")
    private String table_comment;

    @Schema(description = "테이블 소분류 정보")
    private String small_clsf_name;

    @Schema(description = "검색 결과 총 갯수")
    private int total_num;
}
