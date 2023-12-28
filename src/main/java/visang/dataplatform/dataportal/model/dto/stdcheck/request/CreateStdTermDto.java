package visang.dataplatform.dataportal.model.dto.stdcheck.request;

import lombok.Getter;

@Getter
public class CreateStdTermDto {
    private Integer term_idx;
    private String term_logical_nm;
    private String term_logical_desc;
    private String term_physical_nm;
    private String term_physical_desc;
    private Integer domain_idx;
}