package visang.dataplatform.dataportal.model.dto.stdcheck.request;

import lombok.Getter;

@Getter
public class CreateStdWordDto {
    private Integer word_idx;
    private String word_logical_nm;
    private String word_physical_nm;
    private String word_logical_desc;
    private String word_physical_desc;
    private String synonym_list;
}