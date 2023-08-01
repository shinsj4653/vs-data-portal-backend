package visang.dataplatform.dataportal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import visang.dataplatform.dataportal.model.entity.metadata.QueryResponseMeta;
import visang.dataplatform.dataportal.mapper.MetaDataMapper;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetaDataService {

    private final MetaDataMapper metaDataMapper;

    public List<QueryResponseMeta> getMetaDataWithMainCategory(String serviceName, String mainCategoryName) {
        return metaDataMapper.getMetaDataWithMainCategory(serviceName, mainCategoryName);
    }
    public List<QueryResponseMeta> getMetaDataWithSubCategory(String serviceName, String mainCategoryName, String subCategoryName) {
        return metaDataMapper.getMetaDataWithSubCategory(serviceName, mainCategoryName, subCategoryName);
    }

}
