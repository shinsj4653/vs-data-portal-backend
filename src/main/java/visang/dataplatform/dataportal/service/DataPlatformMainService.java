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
import visang.dataplatform.dataportal.utils.ElasticUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataPlatformMainService {

    private final DataPlatformMainMapper dataPlatformMapper;

    public List<DatasetSearchDto> getServiceList(String keyword, Integer pageNo, Integer amountPerPage) {

        // 빈 키워드인지 체크
        validateBlankKeyword(keyword);

        // 검색 시, 검색 로그를 로그스태시로 전송
        if (!(keyword.equals("") || keyword.equals("undefined") || keyword.equals(null) || keyword == null || keyword.equals("null"))) {
            log.info("{} {} {}", keyValue("apiType", "search"), keyValue("requestURI", "/dpmain/search/service-dataset"), keyValue("keyword", keyword));
        }

        log.info("pageNo : {}", pageNo);
        log.info("amountPerPage : {}", amountPerPage);

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

        for (SearchHit hit : searchHits) {
            Map<String, Object> sourceMap = hit.getSourceAsMap();
            result.add(new DatasetSearchDto(String.valueOf(sourceMap.get("service_name")), String.valueOf(sourceMap.get("main_category_name")), String.valueOf(sourceMap.get("sub_category_name")), searchHits.getTotalHits().value));
        }

        return result;


        //return dataPlatformMapper.getServiceList(keyword);
    }

    // 데이터 검색 시, 빈 키워드를 입력하는 경우, 로그 전송 하지 않도록 막기
    private void validateBlankKeyword(String keyword) {
        if (keyword.equals("") || keyword.equals("undefined") || keyword.equals(null) || keyword == null || keyword.equals("null"))
            throw new BlankSearchKeywordException();
    }
}
