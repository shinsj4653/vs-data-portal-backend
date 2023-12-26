package visang.dataplatform.dataportal.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import visang.dataplatform.dataportal.model.dto.stdcheck.request.CreateStdTermDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.request.UpdateStdTermDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.SimpleStdTermDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.StdTermDetailDto;

import java.util.List;

@Mapper
public interface StdTermMapper {

    List<SimpleStdTermDto> getStdTerms(@Param("page_num") int pageNum);

    StdTermDetailDto getStdTerm(@Param("term_idx") long id);

    void createStdTerm(CreateStdTermDto createStdTermDto);

    void updateStdTerm(UpdateStdTermDto updateStdTermDto);

    void deleteStdTerm(@Param("term_idx") long id);

}