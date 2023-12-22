package visang.dataplatform.dataportal.model.dto.stdcheck.request;

import lombok.Getter;

@Getter
public class CreateStdTermDto {
    private String term_logical_nm;
    private String term_logical_desc;
    private String term_physical_nm;
    private String term_physical_desc;
    private Long domain_idx;
}