package visang.dataplatform.dataportal.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import visang.dataplatform.dataportal.model.query.dataorg.QueryResponseAllOrgData;
import visang.dataplatform.dataportal.model.query.dataorg.QueryResponseSystemInfo;

import java.util.List;

@Mapper
public interface DataOrgMapper {
    List<QueryResponseAllOrgData> getAllOrgInfo();
    List<QueryResponseSystemInfo> getSystemInfo(String name);
    List<String> getSystemByTarget(@Param("targetName") String targetName);
}
