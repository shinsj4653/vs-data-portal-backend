package visang.dataplatform.dataportal.model.dto.stdcheck.response;

import lombok.Getter;

@Getter
public class SimpleStdDomainDto {
    private Integer domain_idx;
    private String domain_group_nm;
    private String domain_category_nm;
    private String domain_nm;
    private String data_type;
    private Integer data_length;
}