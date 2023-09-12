package visang.dataplatform.dataportal.model.dto.metadata;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "메타데이터 - 테이블 검색 키워드 순위 DTO")
public class TableSearchKeywordRank {
    private String keyword;
    private int count;
}
