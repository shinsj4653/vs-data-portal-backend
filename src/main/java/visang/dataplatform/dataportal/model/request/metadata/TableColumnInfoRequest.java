package visang.dataplatform.dataportal.model.request.metadata;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "메타 데이터 테이블의 컬럼 정보 요청 Body")
public class TableColumnInfoRequest {
    @Schema(description = "테이블 ID명")
    private String table_id;
}
