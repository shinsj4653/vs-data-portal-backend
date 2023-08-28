package visang.dataplatform.dataportal.model.query.metadata;

import lombok.Getter;

@Getter
public class QueryResponseTableColumnInfo {
    private String table_col_id;
    private String table_col_datatype;
    private String table_col_comment;
}
