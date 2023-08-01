package visang.dataplatform.dataportal.mapper;

import org.apache.ibatis.annotations.Mapper;
import visang.dataplatform.dataportal.dto.response.dataorg.QueryResponseAllOrgData;
import visang.dataplatform.dataportal.dto.response.dataorg.QueryResponseSystemInfo;

import java.util.List;

@Mapper
public interface DataOrgMapper {
    List<QueryResponseAllOrgData> getAllOrgInfo();
    List<QueryResponseSystemInfo> getSystemInfo(String name);
}
