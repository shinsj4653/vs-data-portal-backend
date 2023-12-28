package visang.dataplatform.dataportal.model.dto.stdcheck.response;

import lombok.Getter;

@Getter
public class SimpleStdTermDto {
    private Integer term_idx;
    private String term_logical_nm;
    private String term_physical_nm;
    private String domain_nm;
    private String data_type;
    private String data_length;
}