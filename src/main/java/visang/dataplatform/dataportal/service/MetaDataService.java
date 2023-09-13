package visang.dataplatform.dataportal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import visang.dataplatform.dataportal.model.dto.metadata.TableColumnDto;
import visang.dataplatform.dataportal.model.dto.metadata.TableMetaInfoDto;
import visang.dataplatform.dataportal.model.dto.metadata.TableSearchDto;
import visang.dataplatform.dataportal.model.dto.metadata.TableSearchKeywordRank;
import visang.dataplatform.dataportal.model.query.metadata.QueryResponseMeta;
import visang.dataplatform.dataportal.mapper.MetaDataMapper;
import visang.dataplatform.dataportal.model.query.metadata.QueryResponseTableColumnInfo;
import visang.dataplatform.dataportal.utils.ElasticUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public List<TableSearchDto> getTotalTableSearchResult(String keyword) {

        ElasticUtil client = ElasticUtil.getInstance("localhost", 9200);

        // index : tb_table_meta_info-YYYY.MM.DD
        LocalDate now = LocalDate.now();

        // fields
        List<String> fields = new ArrayList<>();
        fields.add("table_id");
        fields.add("table_comment");
        fields.add("small_clsf_name");

        String indexName = "tb_table_meta_info-" + now;
        List<Map<String, Object>> searchResult = client.getTotalTableSearch(indexName, keyword, fields, 10000);
        log.info("method=GET, requestURI=/metadata/search/total, keyword={}", keyword);

        return searchResult.stream()
                .map(mapData -> new TableSearchDto(String.valueOf(mapData.get("table_id")), String.valueOf(mapData.get("table_comment")), String.valueOf(mapData.get("small_clsf_name")), searchResult.size()))
                .collect(Collectors.toList());
    }

//    public List<TableSearchKeywordRank> getTableSearchRank() {
//
//
//    }

    // QueryResponseMeta에서 TableMetaInfoDto에 필요한 정보만 추출하여 리스트 형태로 반환해주는 함수
    private List<TableMetaInfoDto> makeMetaInfoTree(List<QueryResponseMeta> result) {

        List<TableMetaInfoDto> list = new ArrayList<>();

        for (QueryResponseMeta q : result) {
            TableMetaInfoDto metaData = new TableMetaInfoDto(q.getTable_meta_info_id(), q.getTable_id(), q.getTable_comment(), q.getSmall_clsf_name());
            list.add(metaData);
        }
        return list;
    }
    // QueryResponseTableColumnInfo에서 TableColumnDto로 변환하여 리스트 형태로 반환해주는 함수
    private List<TableColumnDto> makeTableColumnDto(List<QueryResponseTableColumnInfo> result) {

        List<TableColumnDto> list = new ArrayList<>();

        for (QueryResponseTableColumnInfo q : result) {
            TableColumnDto colData = new TableColumnDto(q.getTable_col_id(), q.getTable_col_datatype(), q.getTable_col_comment());
            list.add(colData);
        }
        return list;
    }

}
