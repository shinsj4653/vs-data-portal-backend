package visang.dataplatform.dataportal.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DataByCategoryDto {

    private String company_name;
    private String company_color;
    private Integer company_id;

    @Schema(description = "Service")
    private String service_name;
    private String service_color;
    private Integer service_id;

    @Schema(description = "Main Category")
    private String main_category_name;
    private String main_category_color;
    private Integer main_category_id;

    @Schema(description = "Sub Category")
    private String sub_category_name;
    private String sub_category_color;
    private Integer sub_category_id;

    @Schema(description = "메타 테이블 갯수")
    private Integer loc;

}
