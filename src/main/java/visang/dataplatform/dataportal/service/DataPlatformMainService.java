package visang.dataplatform.dataportal.service;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.stereotype.Service;
import visang.dataplatform.dataportal.exception.badrequest.metadata.BlankSearchKeywordException;
import visang.dataplatform.dataportal.mapper.DataPlatformMainMapper;
import visang.dataplatform.dataportal.model.dto.dpmain.DatasetSearchDto;
import visang.dataplatform.dataportal.model.dto.metadata.TableSearchDto;
import visang.dataplatform.dataportal.model.dto.metadata.TableSearchKeywordRankDto;
import visang.dataplatform.dataportal.model.request.metadata.TableSearchRankRequest;
import visang.dataplatform.dataportal.utils.ElasticUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataPlatformMainService {

    public List<DatasetSearchDto> getServiceList(String keyword, Integer pageNo, Integer amountPerPage) throws IOException {

        // 빈 키워드인지 체크
        validateBlankKeyword(keyword);

        ElasticUtil client = ElasticUtil.getInstance("localhost", 9200);

        // index : tb_table_main_search
        String indexName = "tb_table_main_search";

        // fields : 선택한 검색 기준에 따라 필요한 fields 배열이 다름
        List<String> fields = new ArrayList<>();
        fields.add("service_name_english");
        fields.add("main_category_name_english");
        fields.add("sub_category_name_english");
        fields.add("service_name_korean");
        fields.add("main_category_name_korean");
        fields.add("sub_category_name_korean");

        // ES QueryDSL 검색결과 반환
        SearchHits searchHits = client.getTotalTableSearch(indexName, keyword, fields, pageNo, amountPerPage);
        List<DatasetSearchDto> result = new ArrayList<>();

        // 실시간 검색어에 "의미 있는 단어"만 포함되도록
        // -> table_id, table_comment, small_clsf_name 결과들 중에서, keyword를 포함하고 있을 때만 로그 전송
        boolean hasKeyword = false;

        Long totalHitNum = searchHits.getTotalHits().value;

        for (SearchHit hit : searchHits) {

            Map<String, Object> sourceMap = hit.getSourceAsMap();
            String serviceName = String.valueOf(sourceMap.get("service_name"));
            String mainCategoryName = String.valueOf(sourceMap.get("main_category_name"));
            String subCategoryName = String.valueOf(sourceMap.get("sub_category_name"));

            float score = hit.getScore();

            DatasetSearchDto docData = new DatasetSearchDto(serviceName, mainCategoryName, subCategoryName, totalHitNum, score);

            result.add(docData);

            if (isResultContainsKeyword(docData, keyword)) {
                hasKeyword = true;
            }
        }

        // 검색 정확도(score) 기준으로 result 정렬
        Collections.sort(result, (dto1, dto2) -> Float.compare(dto2.getScore(), dto1.getScore()));

        log.debug("totalHitNum : {}", totalHitNum);
        log.debug("hasKeyword : {}", hasKeyword);

        // 검색 결과가 있는 경우에만 검색 로그 전송
        if (totalHitNum > 0L && hasKeyword) {
            log.info("{} {} {}", keyValue("logType", "search"), keyValue("requestURI", "/dpmain/search/keyword"), keyValue("keyword", keyword));
        }

        return result;

    }

    private static boolean isResultContainsKeyword(DatasetSearchDto doc, String keyword) {
        if (doc.getService_name().replaceAll(" ", "").contains(keyword.replaceAll(" ", ""))
                || doc.getMain_category_name().replaceAll(" ", "").contains(keyword.replaceAll(" ", ""))
                || doc.getSub_category_name().replaceAll(" ", "").contains(keyword.replaceAll(" ", ""))){
            return true;
        }
        return false;
    }

    public List<TableSearchKeywordRankDto> getSearchRank(TableSearchRankRequest request) {

        String requestURI = request.getRequest_uri();
        // api 종류가 검색 api에 해당하는 로그만 집계
        String logType = request.getLog_type();

        // 검색 시간대
        //String gte = request.getGte();
        //String lte = request.getLte();

        ElasticUtil client = ElasticUtil.getInstance("localhost", 9200);

        // 현재 날짜부터 7일 동안의 검색 기록필요
        // last-7-days Alias 사용
        return client.getTableSearchRank(requestURI, logType, 10000, 10);
    }



    // 데이터 검색 시, 빈 키워드를 입력하는 경우, 로그 전송 하지 않도록 막기
    private void validateBlankKeyword(String keyword) {
        if (keyword.equals("") || keyword.equals("undefined") || keyword.equals(null) || keyword == null || keyword.equals("null"))
            throw new BlankSearchKeywordException();
    }
}
