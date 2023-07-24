package visang.dataplatform.dataportal.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import visang.dataplatform.dataportal.dto.response.DataByCategoryDto;

import java.util.List;

@Mapper
public interface DataMapMapper {
    List<DataByCategoryDto> getMapMainData();
    List<DataByCategoryDto> getMapSubData();
    List<String> getPrimaryDataset();
}
