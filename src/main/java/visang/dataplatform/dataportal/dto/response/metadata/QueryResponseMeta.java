package visang.dataplatform.dataportal.dto.response.metadata;

import lombok.Data;

@Data
public class QueryResponseMeta {
    private String main_category_name;

    private String sub_category_id;
    private String sub_category_name;

    private String table_meta_info_id;
    private String table_id;
    private String table_name;
    private String table_comment;
}
