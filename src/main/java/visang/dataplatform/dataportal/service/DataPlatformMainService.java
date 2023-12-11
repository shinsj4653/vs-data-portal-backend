package visang.dataplatform.dataportal.service;

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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataPlatformMainService {

    public List<DatasetSearchDto> getServiceList(String keyword, Integer pageNo, Integer amountPerPage){

        // 빈 키워드인지 체크
        validateBlankKeyword(keyword);

        ElasticUtil client = ElasticUtil.getInstance("localhost", 9200);

        // index : tb_table_main_search
        String indexName = "tb_table_main_search";

        // fields : 선택한 검색 기준에 따라 필요한 fields 배열이 다름
        List<String> fields = new ArrayList<>();
        fields.add("service_name");
        fields.add("main_category_name");
        fields.add("sub_category_name");

        // ES QueryDSL 검색결과 반환
        SearchHits searchHits = client.getTotalTableSearch(indexName, keyword, fields, pageNo, amountPerPage);
        List<DatasetSearchDto> result = new ArrayList<>();

        // 실시간 검색어에 "의미 있는 단어"만 포함되도록
        // -> table_id, table_comment, small_clsf_name 결과들 중에서, keyword를 포함하고 있을 때만 로그 전송
        boolean hasKeyword = false;

        for (SearchHit hit : searchHits) {
            Map<String, Object> sourceMap = hit.getSourceAsMap();
            String serviceName = String.valueOf(sourceMap.get("service_name"));
            String mainCategoryName = String.valueOf(sourceMap.get("main_category_name"));
            String subCategoryName = String.valueOf(sourceMap.get("sub_category_name"));

            result.add(new DatasetSearchDto(serviceName, mainCategoryName, subCategoryName, searchHits.getTotalHits().value));

            if (serviceName.replaceAll(" ", "").contains(keyword.replaceAll(" ", ""))
                    || mainCategoryName.replaceAll(" ", "").contains(keyword.replaceAll(" ", ""))
                    || subCategoryName.replaceAll(" ", "").contains(keyword.replaceAll(" ", ""))) {
                hasKeyword = true;
            }
        }

        // 검색 결과가 있는 경우에만 검색 로그 전송
        if (searchHits.getTotalHits().value > 0 && hasKeyword) {
            log.info("{} {} {}", keyValue("logType", "search"), keyValue("requestURI", "/dpmain/search/keyword"), keyValue("keyword", keyword));
        }

        return result;

    }

    public List<TableSearchKeywordRankDto> getSearchRank(TableSearchRankRequest request) {

        String requestURI = request.getRequestURI();
        // api 종류가 검색 api에 해당하는 로그만 집계
        String logType = request.getLogType();

        // 검색 시간대
        String gte = request.getGte();
        String lte = request.getLte();

        ElasticUtil client = ElasticUtil.getInstance("localhost", 9200);

        // 현재 날짜부터 7일 동안의 검색 기록필요
        // last-7-days Alias 사용
        return client.getTableSearchRank(requestURI, logType, gte, lte, 10000, 10);
    }



    // 데이터 검색 시, 빈 키워드를 입력하는 경우, 로그 전송 하지 않도록 막기
    private void validateBlankKeyword(String keyword) {
        if (keyword.equals("") || keyword.equals("undefined") || keyword.equals(null) || keyword == null || keyword.equals("null"))
            throw new BlankSearchKeywordException();
    }
}
