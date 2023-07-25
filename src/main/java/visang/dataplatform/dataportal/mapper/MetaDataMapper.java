package visang.dataplatform.dataportal.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MetaDataMapper {
    List<String> getMainCategory(@Param("serviceName") String serviceName, @Param("mainCategoryName") String mainCategoryName);
}
