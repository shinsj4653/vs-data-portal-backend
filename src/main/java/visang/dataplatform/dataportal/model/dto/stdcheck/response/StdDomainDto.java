package visang.dataplatform.dataportal.model.dto.stdcheck.response;

import lombok.Getter;

@Getter
public class StdDomainDto {
    private Long domain_idx;
    private String domain_group_nm;
    private String domain_category_nm;
    private String domain_nm;
    private String domain_desc;
    private String data_type;
    private String data_length;
    private String decimal_point_length;
    private String store_format;
    private String represent_format;
    private String allowed_values;
}