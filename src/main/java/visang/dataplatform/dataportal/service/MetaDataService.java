package visang.dataplatform.dataportal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import visang.dataplatform.dataportal.model.dto.metadata.TableSearchDto;
import visang.dataplatform.dataportal.model.entity.metadata.QueryResponseMeta;
import visang.dataplatform.dataportal.mapper.MetaDataMapper;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetaDataService {

    private final MetaDataMapper metaDataMapper;

    public List<String> getMainDataset(String serviceName, Integer limit) {
        return metaDataMapper.getMainDataset(serviceName, limit);
    }
    public List<String> getSubDataset(String serviceName, String mainCategoryName, Integer limit) {
        return metaDataMapper.getSubDataset(serviceName, mainCategoryName, limit);
    }
    public List<QueryResponseMeta> getMetaDataWithMainCategory(String serviceName, String mainCategoryName) {
        return metaDataMapper.getMetaDataWithMainCategory(serviceName, mainCategoryName);
    }
    public List<QueryResponseMeta> getMetaDataWithSubCategory(String serviceName, String mainCategoryName, String subCategoryName) {
        return metaDataMapper.getMetaDataWithSubCategory(serviceName, mainCategoryName, subCategoryName);
    }

    public List<TableSearchDto> getTableSearchResult(String serviceName, String tableKeyword, Integer pageNo, Integer amountPerPage){
        return metaDataMapper.getTableSearchResult(serviceName, tableKeyword, pageNo, amountPerPage);
    }

}
