package visang.dataplatform.dataportal.model.query.stdcheck;

import lombok.Getter;

@Getter
public class StdWord {
    private Long word_idx;
    private String word_logical_nm;
    private String word_physical_nm;
    private String word_logical_desc;
    private String word_physical_desc;
    private String domain_category_nm;
    private String synonym_list;
}