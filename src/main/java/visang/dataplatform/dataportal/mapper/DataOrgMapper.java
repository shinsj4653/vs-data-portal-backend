package visang.dataplatform.dataportal.mapper;

import org.apache.ibatis.annotations.Mapper;
import visang.dataplatform.dataportal.dto.response.dataorg.DataOrgSystemInfoDto;

import java.util.List;

@Mapper
public interface DataOrgMapper {
    List<DataOrgSystemInfoDto> getSystemInfo(String name);
}
