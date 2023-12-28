package visang.dataplatform.dataportal.model.dto.stdcheck.request;

import lombok.Getter;

@Getter
public class CreateStdDomainDto {
    private Integer domain_idx;
    private Integer domain_category_idx;
    private String domain_nm;
    private String domain_desc;
    private String data_type;
    private Integer data_length;
    private Integer decimal_point_length;
    private String store_format;
    private String represent_format;
    private String allowed_values;
}