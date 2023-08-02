package visang.dataplatform.dataportal.mapper;

import org.apache.ibatis.annotations.Mapper;
import visang.dataplatform.dataportal.model.entity.dataorg.QueryResponseAllOrgData;
import visang.dataplatform.dataportal.model.entity.dataorg.QueryResponseSystemInfo;

import java.util.List;

@Mapper
public interface DataOrgMapper {
    List<QueryResponseAllOrgData> getAllOrgInfo();
    List<QueryResponseSystemInfo> getSystemInfo(String name);
}
