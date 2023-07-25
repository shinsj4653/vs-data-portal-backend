package visang.dataplatform.dataportal.dto.response.dataorg;

import lombok.Data;

@Data
public class DataOrgSystemInfoDto {
    private String company_name;
    private String service_name;
    private String service_web_url;
    private String service_os;
    private String service_was;
    private String service_db;
}
