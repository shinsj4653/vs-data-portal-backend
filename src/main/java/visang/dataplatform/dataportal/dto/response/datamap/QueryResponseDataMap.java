package visang.dataplatform.dataportal.dto.response.datamap;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class QueryResponseDataMap {

    private String company_name;
    private String company_color;

    @Schema(description = "Service")
    private String service_name;
    private String service_color;

    @Schema(description = "Main Category")
    private String main_category_name;
    private String main_category_color;

    @Schema(description = "Sub Category")
    private String sub_category_name;
    private String sub_category_color;

}
