package visang.dataplatform.dataportal.dto.request.metadata;

import lombok.Data;

@Data
public class MetaDataRequest {
    private String service_name;
    private String main_category_name;
    private String sub_category_name;
}
