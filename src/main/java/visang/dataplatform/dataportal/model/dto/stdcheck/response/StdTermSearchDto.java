package visang.dataplatform.dataportal.model.dto.stdcheck.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StdTermSearchDto {
    private Integer term_idx;
    private String term_logical_nm;
    private String term_logical_desc;
    private String term_physical_nm;
    private String term_physical_desc;
    private String domain_nm;
    private String data_type;
    private String data_length;
}