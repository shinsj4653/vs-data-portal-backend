package visang.dataplatform.dataportal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public List<TableMetaInfoDto> getMetaDataWithSubCategory(String serviceName, String mainCategoryName, String subCategoryName) {
        List<QueryResponseMeta> res = metaDataMapper.getMetaDataWithSubCategory(serviceName, mainCategoryName, subCategoryName);
        return makeMetaInfoTree(res);
    }

    public List<TableSearchDto> getTableSearchResult(String searchCondition, String keyword, Integer pageNo, Integer amountPerPage) {

        // 빈 키워드인지 체크
        validateBlankKeyword(keyword);

        
        // 검색 시, 검색 로그를 로그스태시로 전송
        if (!(keyword.equals("") || keyword.equals("undefined") || keyword.equals(null) || keyword == null || keyword.equals("null"))) {
            log.info("{} {}", keyValue("requestURI", "/metadata/search/keyword"), keyValue("keyword", keyword));
        }

        log.info("pageNo : {}", pageNo);
        log.info("amountPerPage : {}", amountPerPage);

        ElasticUtil client = ElasticUtil.getInstance("localhost", 9200);

        // index : tb_table_meta_info-YYYY-MM-DD
        LocalDate now = LocalDate.now();
        String indexName = "tb_table_meta_info-" + now;

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
        List<TableSearchDto> searchResult = client.getTotalTableSearch(indexName, keyword, fields, pageNo, amountPerPage);

        return searchResult;

//        return metaDataMapper.getTableSearchResult(serviceName, searchCondition, keyword, pageNo, amountPerPage);
    }

    public List<TableColumnDto> getTableColumnInfo(String tableId) {
        List<QueryResponseTableColumnInfo> list = metaDataMapper.getTableColumnInfo(tableId);
        return makeTableColumnDto(list);
    }

//    public List<TableSearchDto> getTotalTableSearchResult(String keyword) {
//
//        // ci/cd restart test
//
//
//
//        ElasticUtil client = ElasticUtil.getInstance("localhost", 9200);
//
//        // index : tb_table_meta_info-YYYY-MM-DD
//        LocalDate now = LocalDate.now();
//
//        // fields
//        List<String> fields = new ArrayList<>();
//        fields.add("table_id");
//        fields.add("table_comment");
//        fields.add("small_clsf_name");
//
//        String indexName = "tb_table_meta_info-" + now;
//        List<Map<String, Object>> searchResult = client.getTotalTableSearch(indexName, keyword, fields, 10000);
//
//        if (!(keyword.equals("") || keyword.equals("undefined") || keyword.equals(null) || keyword == null || keyword.equals("null"))) {
//            log.info("{} {}", keyValue("requestURI", "/metadata/search/total"), keyValue("keyword", keyword));
//        }
//
//        // 검색 결과 -> TableSearchDto로 감싸주는 작업
//        return searchResult.stream()
//                .map(mapData -> new TableSearchDto(String.valueOf(mapData.get("table_id")), String.valueOf(mapData.get("table_comment")), String.valueOf(mapData.get("small_clsf_name")), searchResult.size()))
//                .collect(Collectors.toList());
//
//
//        //return metaDataMapper.getTableTotalSearchFullScan(keyword);
//    }

    public List<TableSearchKeywordRankDto> getTableSearchRank(TableSearchRankRequest request) {

        // message 안에 uri 가 포함된 로그만 필터링
        String uri = request.getUri();

        // 검색 시간대
        String gte = request.getGte();
        String lte = request.getLte();

        ElasticUtil client = ElasticUtil.getInstance("localhost", 9200);

        // index : metadata_search_log-YYYY-MM-DD
        LocalDate now = LocalDate.now();

        String indexName = "metadata_search_log-" + now;
        return client.getTableSearchRank(indexName, uri, gte, lte, 10000, 10);
    }

    // QueryResponseMeta에서 TableMetaInfoDto에 필요한 정보만 추출하여 리스트 형태로 반환해주는 함수
    private List<TableMetaInfoDto> makeMetaInfoTree(List<QueryResponseMeta> result) {

        List<TableMetaInfoDto> list = new ArrayList<>();

        for (QueryResponseMeta q : result) {
            TableMetaInfoDto metaData = new TableMetaInfoDto(q.getTable_meta_info_id(), q.getTable_id(), q.getTable_comment(), q.getSmall_clsf_name(), list.size());
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
