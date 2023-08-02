package visang.dataplatform.dataportal.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import visang.dataplatform.dataportal.model.entity.metadata.QueryResponseMeta;

import java.util.List;

@Mapper
public interface MetaDataMapper {
    List<QueryResponseMeta> getMetaDataWithMainCategory(@Param("serviceName") String serviceName, @Param("mainCategoryName") String mainCategoryName);
    List<QueryResponseMeta> getMetaDataWithSubCategory(@Param("serviceName") String serviceName, @Param("mainCategoryName") String mainCategoryName , @Param("subCategoryName") String subCategoryName);
}
