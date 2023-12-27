package visang.dataplatform.dataportal.model.dto.stdcheck.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StdWordSearchDto {
    private Integer word_idx;
    private String word_logical_nm;
    private String word_physical_nm;
    private String word_logical_desc;
    private String word_physical_desc;
    private String synonym_list;
}