package visang.dataplatform.dataportal.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import visang.dataplatform.dataportal.model.dto.dpmain.DatasetSearchDto;

import java.util.List;

@Mapper
public interface DataPlatformMainMapper {
    List<DatasetSearchDto> getServiceList(@Param("keyword") String keyword);
}
