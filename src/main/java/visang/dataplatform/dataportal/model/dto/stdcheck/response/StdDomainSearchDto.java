package visang.dataplatform.dataportal.model.dto.stdcheck.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StdDomainSearchDto {
    private Integer domain_idx;
    private String domain_group_nm;
    private String domain_category_nm;
    private String domain_nm;
    private String domain_desc;
    private String data_type;
    private Integer data_length;
}