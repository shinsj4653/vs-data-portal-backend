package visang.dataplatform.dataportal.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import visang.dataplatform.dataportal.model.dto.metadata.TableSearchDto;
import visang.dataplatform.dataportal.model.query.metadata.QueryResponseMeta;

import java.util.List;

@Mapper
public interface MetaDataMapper {
    List<String> getMainDataset(@Param("serviceName") String serviceName, @Param("limit") Integer limit);
    List<String> getSubDataset(@Param("serviceName") String serviceName, @Param("mainCategoryName") String mainCategoryName, @Param("limit") Integer limit);
    List<QueryResponseMeta> getMetaDataWithSubCategory(@Param("serviceName") String serviceName, @Param("mainCategoryName") String mainCategoryName , @Param("subCategoryName") String subCategoryName);
    List<TableSearchDto> getTableSearchResult(@Param("serviceName") String serviceName, @Param("searchCondition") String searchCondition, @Param("tableKeyword") String tableKeyword, @Param("pageNo") Integer pageNo, @Param("amountPerPage") Integer amountPerPage);
}
