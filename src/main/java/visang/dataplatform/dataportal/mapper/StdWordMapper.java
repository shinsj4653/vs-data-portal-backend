package visang.dataplatform.dataportal.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import visang.dataplatform.dataportal.model.dto.stdcheck.request.CreateStdWordDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.request.UpdateStdWordDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.SimpleStdWordDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.StdWordDetailDto;

import java.util.List;

@Mapper
public interface StdWordMapper {

    List<SimpleStdWordDto> getStdWords(@Param("pageNum") int pageNum);

    StdWordDetailDto getStdWord(@Param("word_idx") long id);

    void createStdWord(CreateStdWordDto createStdWordDto);

    void updateStdWord(UpdateStdWordDto updateStdWordDto);

    void deleteStdWord(@Param("word_idx") long id);

}