package visang.dataplatform.dataportal.model.query.stdcheck;

import lombok.Getter;

@Getter
public class StdTerm {
    private Long term_idx;
    private String term_logical_nm;
    private String term_logical_desc;
    private String term_physical_nm;
    private String term_physical_desc;
    private String domain_nm;
    private String data_type;
    private String data_length;
    private String allowed_values;
    private String store_format;
    private String represent_format;
}