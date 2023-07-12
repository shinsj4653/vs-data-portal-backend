package visang.dataportal.test.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DataBySubjectDto {

    private String company_name;
    private String company_color;
    private int company_id;

    private String service_name;
    private String service_color;
    private int service_id;

    private String main_subject_name;
    private String main_subject_color;
    private int main_subject_id;

    private String sub_subject_name;
    private String sub_subject_color;
    private int sub_subject_id;

    private int loc;

}
