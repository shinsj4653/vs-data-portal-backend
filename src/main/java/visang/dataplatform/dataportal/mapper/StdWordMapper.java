package visang.dataplatform.dataportal.mapper;

import org.apache.ibatis.annotations.Param;
import visang.dataplatform.dataportal.model.query.stdcheck.StdWord;

import java.util.List;

public interface StdWordMapper {
    List<StdWord> getStdWords(@Param("pageNum") int pageNum);

    StdWord getStdWord(@Param("id") long id);

    void createStdWord();

    void updateStdWord();

    void deleteStdWord(@Param("id") long id);

}