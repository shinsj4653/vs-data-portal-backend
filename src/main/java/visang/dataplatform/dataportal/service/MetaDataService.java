package visang.dataplatform.dataportal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import visang.dataplatform.dataportal.exception.badrequest.metadata.BlankSearchKeywordException;
import visang.dataplatform.dataportal.model.dto.metadata.TableColumnDto;
import visang.dataplatform.dataportal.model.dto.metadata.TableMetaInfoDto;
import visang.dataplatform.dataportal.model.dto.metadata.TableSearchDto;
import visang.dataplatform.dataportal.model.dto.metadata.TableSearchKeywordRankDto;
import visang.dataplatform.dataportal.model.query.metadata.QueryResponseMeta;
import visang.dataplatform.dataportal.mapper.MetaDataMapper;
import visang.dataplatform.dataportal.model.query.metadata.QueryResponseTableColumnInfo;
import visang.dataplatform.dataportal.model.request.metadata.TableSearchRankRequest;
import visang.dataplatform.dataportal.utils.ElasticUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

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

    public List<TableMetaInfoDto> getMetaDataWithSubCategory(String serviceName, String mainCategoryName, String subCategoryName, Integer pageNo, Integer amountPerPage) {

        log.info("mainCategoryName == ", mainCategoryName);
        if (mainCategoryName.equals("") || mainCategoryName.equals("undefined") || mainCategoryName.equals(null) || mainCategoryName == null || mainCategoryName.equals("null")){
            return new ArrayList<>();
        }

        List<QueryResponseMeta> res = metaDataMapper.getMetaDataWithSubCategory(serviceName, mainCategoryName, subCategoryName);
        return makeMetaInfoTree(res, pageNo, amountPerPage);
    }

    public List<TableSearchDto> getTableSearchResult(String searchCondition, String keyword, Integer pageNo, Integer amountPerPage) {

        // 빈 키워드인지 체크
        //validateBlankKeyword(keyword);

        log.info("pageNo : {}", pageNo);
        log.info("amountPerPage : {}", amountPerPage);

        ElasticUtil client = ElasticUtil.getInstance("localhost", 9200);

        // index : tb_table_meta_info
        String indexName = "tb_table_meta_info";

        // fields : 선택한 검색 기준에 따라 필요한 fields 배열이 다름
        List<String> fields = new ArrayList<>();

        if (searchCondition.equals("table_id") || searchCondition.equals("total")) {
            fields.add("table_id");
        }
        if (searchCondition.equals("table_comment") || searchCondition.equals("total")) {
            fields.add("table_comment");
        }
        if (searchCondition.equals("small_clsf_name") || searchCondition.equals("total")) {
            fields.add("small_clsf_name");
        }
        
        // ES QueryDSL 검색결과 반환
        SearchHits searchHits = client.getTotalTableSearch(indexName, keyword, fields, pageNo, amountPerPage);
        List<TableSearchDto> result = new ArrayList<>();

        // 검색 결과가 있는 경우에만 검색 로그 전송
        if (searchHits.getTotalHits().value > 0) {
            log.info("{} {} {}", keyValue("apiType", "search"), keyValue("requestURI", "/metadata/search/keyword"), keyValue("keyword", keyword));
        }

        // 검색 결과 -> TableSearchDto로 감싸주는 작업
        for (SearchHit hit : searchHits) {
            Map<String, Object> sourceMap = hit.getSourceAsMap();
            result.add(new TableSearchDto(String.valueOf(sourceMap.get("table_id")), String.valueOf(sourceMap.get("table_comment")), String.valueOf(sourceMap.get("small_clsf_name")), searchHits.getTotalHits().value));
        }

        return result;

//        return metaDataMapper.getTableSearchResult(serviceName, searchCondition, keyword, pageNo, amountPerPage);
    }

    public List<String> getAutoCompleteSearchWords(String index, List<String> searchConditions, String keyword) {
        ElasticUtil client = ElasticUtil.getInstance("localhost", 9200);
        // 중복 제거 위한 Set
        Set<String> result = new LinkedHashSet<>();

        for (String searchCondition : searchConditions) {
            SearchHits searchHits = client.getAutoCompleteSearchWords(index, searchCondition, keyword);

            // 결과 json 리스트에서, 단어 가져오기
            for (SearchHit hit : searchHits) {
                Map<String, Object> sourceMap = hit.getSourceAsMap();
                result.add(String.valueOf(sourceMap.get(searchCondition)));
            }
        }
        // 중복 제거 완료된 set을 리스트 형태로 변환하여 return
        return new ArrayList<>(result);

    }

    public List<TableColumnDto> getTableColumnInfo(String tableId) {
        List<QueryResponseTableColumnInfo> list = metaDataMapper.getTableColumnInfo(tableId);
        return makeTableColumnDto(list);
    }



    // QueryResponseMeta에서 TableMetaInfoDto에 필요한 정보만 추출하여 리스트 형태로 반환해주는 함수
    private List<TableMetaInfoDto> makeMetaInfoTree(List<QueryResponseMeta> result, Integer pageNo, Integer amountPerPage) {

        List<TableMetaInfoDto> list = new ArrayList<>();
        int startIdx = (pageNo - 1) * amountPerPage;
        int endIdx = startIdx + amountPerPage;
        
        // 만약 마지막 인덱스가 result 크기 넘어갈 경우, size() 값으로 마지막 인덱스 세팅
        if (startIdx + amountPerPage > result.size()) {
            endIdx = result.size();
        }

        for (int idx = startIdx; idx < endIdx; idx++) {
            QueryResponseMeta q = result.get(idx);
            TableMetaInfoDto metaData = new TableMetaInfoDto(q.getTable_meta_info_id(), q.getTable_id(), q.getTable_comment(), q.getSmall_clsf_name(), result.size());
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

    // 메타 데이터 검색 시, 빈 키워드를 입력하는 경우, 로그 전송 하지 않도록 막기
    private void validateBlankKeyword(String keyword) {
        if (keyword.equals("") || keyword.equals("undefined") || keyword.equals(null) || keyword == null || keyword.equals("null"))
            throw new BlankSearchKeywordException();
    }
}
