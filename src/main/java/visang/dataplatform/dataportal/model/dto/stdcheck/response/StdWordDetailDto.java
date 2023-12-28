package visang.dataplatform.dataportal.model.dto.stdcheck.response;

import lombok.Getter;

@Getter
public class StdWordDetailDto {
    private Integer word_idx;
    private String word_logical_nm;
    private String word_physical_nm;
    private String word_logical_desc;
    private String word_physical_desc;
    private String synonym_list;
}