package visang.dataplatform.dataportal.model.dto.stdcheck.response;

import lombok.Getter;

@Getter
public class StdDomainDetailDto {
    private Long domain_idx;
    private String domain_group_nm;
    private String domain_category_nm;
    private String domain_nm;
    private String domain_desc;
    private String data_type;
    private Integer data_length;
    private Integer decimal_point_length;
    private String store_format;
    private String represent_format;
}