package visang.dataplatform.dataportal.mapper;

import org.apache.ibatis.annotations.Mapper;
import visang.dataplatform.dataportal.model.entity.datamap.QueryResponseDataMap;

import java.util.List;

@Mapper
public interface DataMapMapper {
    List<QueryResponseDataMap> getMapMainData();
    List<QueryResponseDataMap> getMapSubData();
    List<String> getPrimaryDataset();
}
