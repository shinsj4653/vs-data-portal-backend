package visang.dataplatform.dataportal.dto.response.metadata;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TableMetaInfoDto {
    private String table_meta_info_id;
    private String table_id;
    private String table_name;
    private String table_comment;
}
