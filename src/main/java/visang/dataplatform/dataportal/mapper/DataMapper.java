package visang.dataplatform.dataportal.mapper;

import org.apache.ibatis.annotations.Mapper;
import visang.dataplatform.dataportal.dto.DataByCategoryDto;

import java.util.List;

@Mapper
public interface DataMapper {
    List<DataByCategoryDto> getChartData();

}
