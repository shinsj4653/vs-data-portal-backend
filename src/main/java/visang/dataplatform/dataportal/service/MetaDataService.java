package visang.dataplatform.dataportal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import visang.dataplatform.dataportal.model.dto.metadata.TableColumnDto;
import visang.dataplatform.dataportal.model.dto.metadata.TableMetaInfoDto;
import visang.dataplatform.dataportal.model.dto.metadata.TableSearchDto;
import visang.dataplatform.dataportal.model.query.metadata.QueryResponseMeta;
import visang.dataplatform.dataportal.mapper.MetaDataMapper;
import visang.dataplatform.dataportal.model.query.metadata.QueryResponseTableColumnInfo;

import java.util.ArrayList;
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
    public List<TableMetaInfoDto> getMetaDataWithSubCategory(String serviceName, String mainCategoryName, String subCategoryName) {
        List<QueryResponseMeta> res = metaDataMapper.getMetaDataWithSubCategory(serviceName, mainCategoryName, subCategoryName);
        return makeMetaInfoTree(res);
    }

    public List<TableSearchDto> getTableSearchResult(String serviceName, String searchCondition, String tableKeyword, Integer pageNo, Integer amountPerPage){
        return metaDataMapper.getTableSearchResult(serviceName, searchCondition, tableKeyword, pageNo, amountPerPage);
    }

    public List<TableColumnDto> getTableColumnInfo(String tableId) {
        List<QueryResponseTableColumnInfo> list = metaDataMapper.getTableColumnInfo(tableId);
        return makeTableColumnDto(list);
    }

    // QueryResponseMeta에서 TableMetaInfoDto에 필요한 정보만 추출하여 리스트 형태로 반환해주는 함수
    private List<TableMetaInfoDto> makeMetaInfoTree(List<QueryResponseMeta> result) {

        List<TableMetaInfoDto> list = new ArrayList<>();

        for (QueryResponseMeta q : result) {
            TableMetaInfoDto metaData = new TableMetaInfoDto(q.getTable_meta_info_id(), q.getTable_id(), q.getTable_name(), q.getTable_comment(), q.getSmall_clsf_name());
            list.add(metaData);
        }
        return list;
    }
    // QueryResponseTableColumnInfo에서 TableColumnDto로 변환하여 리스트 형태로 반환해주는 함수
    private List<TableColumnDto> makeTableColumnDto(List<QueryResponseTableColumnInfo> result) {

        List<TableColumnDto> list = new ArrayList<>();

        for (QueryResponseTableColumnInfo q : result) {
            TableColumnDto colData = new TableColumnDto(q.getTable_col_id(), q.getTable_col_name(), q.getTable_col_datatype(), q.getTable_col_comment());
            list.add(colData);
        }
        return list;
    }


}
