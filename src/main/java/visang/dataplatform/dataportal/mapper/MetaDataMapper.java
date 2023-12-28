package visang.dataplatform.dataportal.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import visang.dataplatform.dataportal.model.dto.metadata.TableSearchDto;
import visang.dataplatform.dataportal.model.query.metadata.QueryResponseMeta;
import visang.dataplatform.dataportal.model.query.metadata.QueryResponseTableColumnInfo;

import java.util.List;

@Mapper
public interface MetaDataMapper {
    List<String> getMainDataset(@Param("serviceName") String serviceName, @Param("limit") Integer limit);
    List<String> getSubDataset(@Param("serviceName") String serviceName, @Param("mainCategoryName") String mainCategoryName, @Param("limit") Integer limit);
    List<QueryResponseMeta> getMetaDataWithSubCategory(@Param("serviceName") String serviceName, @Param("mainCategoryName") String mainCategoryName, @Param("subCategoryName") String subCategoryName);
    List<QueryResponseTableColumnInfo> getTableColumnInfo(@Param("tableId") String tableId);
}
