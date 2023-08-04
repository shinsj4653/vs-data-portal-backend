package visang.dataplatform.dataportal.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import visang.dataplatform.dataportal.model.entity.metadata.QueryResponseMeta;

import java.util.List;

@Mapper
public interface MetaDataMapper {
    List<String> getMainDataset(@Param("serviceName") String serviceName, @Param("limit") Integer limit);
    List<String> getSubDataset(@Param("serviceName") String serviceName, @Param("limit") Integer limit);

    List<QueryResponseMeta> getMetaDataWithMainCategory(@Param("serviceName") String serviceName, @Param("mainCategoryName") String mainCategoryName);
    List<QueryResponseMeta> getMetaDataWithSubCategory(@Param("serviceName") String serviceName, @Param("mainCategoryName") String mainCategoryName , @Param("subCategoryName") String subCategoryName, @Param("amount") Integer amount, @Param("pageNo") Integer pageNo);


}
