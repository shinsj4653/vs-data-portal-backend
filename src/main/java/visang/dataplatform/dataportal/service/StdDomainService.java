package visang.dataplatform.dataportal.service;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import visang.dataplatform.dataportal.mapper.StdDomainMapper;
import visang.dataplatform.dataportal.model.dto.stdcheck.request.CreateStdDomainDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.request.UpdateStdDomainDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.SimpleStdDomainDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.StdDomainDetailDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.StdDomainSearchDto;
import visang.dataplatform.dataportal.utils.ElasticUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StdDomainService {

    private final StdDomainMapper stdDomainMapper;

    @Transactional(readOnly = true)
    public List<SimpleStdDomainDto> getStdDomains(int pageNum) {
        return stdDomainMapper.getStdDomains(pageNum);
    }

    @Transactional(readOnly = true)
    public StdDomainDetailDto getStdDomain(Long domain_idx) {
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
    public void deleteStdDomain(Long domain_idx) {
        stdDomainMapper.deleteStdDomain(domain_idx);
    }

    public List<StdDomainSearchDto> searchStdDomain(int pageNum, String keyword) throws IOException {

        ElasticUtil client = ElasticUtil.getInstance("localhost", 9200);
        String indexName = "tb_std_domain";
        List<String> fields = List.of("domain_group_nm", "domain_category_nm", "domain_nm");

        // QueryDSL 검색결과 반환
        SearchResponse<StdDomainSearchDto> searchHits = client.getTotalTableSearch(indexName, keyword, fields, pageNum, 10, StdDomainSearchDto.class);
        List<StdDomainSearchDto> result = new ArrayList<>();

        // 검색결과를 TableSearchDto로 wrapping
        for (Hit<StdDomainSearchDto> hit : searchHits.hits().hits()) {

            Long domain_idx = hit.source().getDomain_idx();
            String domain_group_nm = hit.source().getDomain_group_nm();
            String domain_category_nm = hit.source().getDomain_category_nm();
            String domain_nm = hit.source().getDomain_nm();
            String domain_desc = hit.source().getDomain_desc();
            String data_type = hit.source().getData_type();
            Integer data_length = hit.source().getData_length();

            StdDomainSearchDto docData = new StdDomainSearchDto(domain_idx, domain_group_nm, domain_category_nm,
                    domain_nm, domain_desc, data_type, data_length);

            result.add(docData);
        }

        return result;
    }
}