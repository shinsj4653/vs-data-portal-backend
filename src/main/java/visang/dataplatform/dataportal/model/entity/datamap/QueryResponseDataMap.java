package visang.dataplatform.dataportal.model.entity.datamap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class QueryResponseDataMap {
    private String company_name;
    private String company_color;

    private String service_name;
    private String service_color;

    private String main_category_name;
    private String main_category_color;

    private String sub_category_name;
    private String sub_category_color;

}
