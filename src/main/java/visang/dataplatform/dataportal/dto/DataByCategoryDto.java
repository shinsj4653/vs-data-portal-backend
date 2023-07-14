package visang.dataplatform.dataportal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DataByCategoryDto {

    private String company_name;
    private String company_color;
    private int company_id;

    private String service_name;
    private String service_color;
    private int service_id;

    private String main_category_name;
    private String main_category_color;
    private int main_category_id;

    private String sub_category_name;
    private String sub_category_color;
    private int sub_category_id;

    private int loc;

}
