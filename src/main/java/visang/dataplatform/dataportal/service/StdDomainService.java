package visang.dataplatform.dataportal.service;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import visang.dataplatform.dataportal.mapper.StdDomainMapper;
import visang.dataplatform.dataportal.model.dto.stdcheck.request.CreateStdDomainDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.request.UpdateStdDomainDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.SimpleStdDomainDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.StdDomainDetailDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.StdDomainSearchDto;
import visang.dataplatform.dataportal.utils.ElasticUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StdDomainService {

    private final StdDomainMapper stdDomainMapper;

    @Transactional(readOnly = true)
    public List<SimpleStdDomainDto> getStdDomains(int pageNum) {
        return stdDomainMapper.getStdDomains(pageNum);
    }

    @Transactional(readOnly = true)
    public StdDomainDetailDto getStdDomain(int domain_idx) {
        return stdDomainMapper.getStdDomain(domain_idx);
    }

    @Transactional
    public void createStdDomain(CreateStdDomainDto createStdDomainDto) {
        stdDomainMapper.createStdDomain(createStdDomainDto);
    }

    @Transactional
    public void updateStdDomain(UpdateStdDomainDto updateStdDomainDto) {
        stdDomainMapper.updateStdDomain(updateStdDomainDto);
    }

    @Transactional
    public void deleteStdDomain(int domain_idx) {
        stdDomainMapper.deleteStdDomain(domain_idx);
    }

    public List<StdDomainSearchDto> searchStdDomain(int pageNum, String keyword) {

        ElasticUtil client = ElasticUtil.getInstance("localhost", 9200);
        String indexName = "tb_std_domain";
        List<String> fields = List.of("domain_group_nm", "domain_category_nm", "domain_nm");

        // QueryDSL 검색결과 반환
        SearchHits searchHits = client.searchStdTable(indexName, keyword, fields, pageNum, 10);
        List<StdDomainSearchDto> result = new ArrayList<>();

        // 검색결과를 TableSearchDto로 wrapping
        for (SearchHit hit : searchHits) {

            Map<String, Object> sourceMap = hit.getSourceAsMap();
            Integer domain_idx = (Integer) sourceMap.get("domain_idx");
            String domain_group_nm = (String) sourceMap.get("domain_group_nm");
            String domain_category_nm = (String) sourceMap.get("domain_category_nm");
            String domain_nm = (String) sourceMap.get("domain_nm");
            String domain_desc = (String) sourceMap.get("domain_desc");
            String data_type = (String) sourceMap.get("data_type");
            Integer data_length = (Integer) sourceMap.get("data_length");

            StdDomainSearchDto docData = new StdDomainSearchDto(domain_idx, domain_group_nm, domain_category_nm,
                    domain_nm, domain_desc, data_type, data_length);

            result.add(docData);
        }

        return result;
    }
}