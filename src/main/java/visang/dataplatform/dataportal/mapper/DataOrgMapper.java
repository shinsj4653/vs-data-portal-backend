package visang.dataplatform.dataportal.mapper;

import org.apache.ibatis.annotations.Mapper;
import visang.dataplatform.dataportal.dto.response.dataorg.QueryResponseDataOrg;

import java.util.List;

@Mapper
public interface DataOrgMapper {
    List<QueryResponseDataOrg> getSystemInfo(String name);
}
