package visang.dataplatform.dataportal.service;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import visang.dataplatform.dataportal.mapper.StdTermMapper;
import visang.dataplatform.dataportal.model.dto.stdcheck.request.CreateStdTermDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.request.UpdateStdTermDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.SimpleStdTermDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.StdTermDetailDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.StdTermSearchDto;
import visang.dataplatform.dataportal.utils.ElasticUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StdTermService {

    private final StdTermMapper stdTermMapper;

    @Transactional(readOnly = true)
    public List<SimpleStdTermDto> getStdTerms(int pageNum) {
        return stdTermMapper.getStdTerms(pageNum);
    }

    @Transactional(readOnly = true)
    public StdTermDetailDto getStdTerm(Long word_idx) {
        return stdTermMapper.getStdTerm(word_idx);
    }

    @Transactional
    public void createStdTerm(CreateStdTermDto createStdTermDto) {
        stdTermMapper.createStdTerm(createStdTermDto);
    }

    @Transactional
    public void updateStdTerm(UpdateStdTermDto updateStdTermDto) {
        stdTermMapper.updateStdTerm(updateStdTermDto);
    }

    @Transactional
    public void deleteStdTerm(Long term_idx) {
        stdTermMapper.deleteStdTerm(term_idx);
    }

    public List<StdTermSearchDto> searchStdTerm(int pageNum, String keyword) throws IOException {

        ElasticUtil client = ElasticUtil.getInstance("localhost", 9200);
        String indexName = "tb_std_term";
        List<String> fields = List.of("term_logical_nm", "term_physical_nm");

        // QueryDSL 검색결과 반환
        SearchResponse<StdTermSearchDto> searchHits = client.getTotalTableSearch(indexName, keyword, fields, pageNum, 10, StdTermSearchDto.class);
        List<StdTermSearchDto> result = new ArrayList<>();

        // 검색결과를 TableSearchDto로 wrapping
        for (Hit<StdTermSearchDto> hit : searchHits.hits().hits()) {

            Long term_idx = hit.source().getTerm_idx();
            String term_logical_nm = hit.source().getTerm_logical_nm();
            String term_logical_desc = hit.source().getTerm_logical_desc();
            String term_physical_nm = hit.source().getTerm_physical_nm();
            String term_physical_desc = hit.source().getTerm_physical_desc();
            String domain_nm = hit.source().getDomain_nm();
            String data_type = hit.source().getData_type();
            String data_length = hit.source().getData_length();

            StdTermSearchDto docData = new StdTermSearchDto(term_idx, term_logical_nm, term_logical_desc,
                    term_physical_nm, term_physical_desc, domain_nm, data_type, data_length);

            result.add(docData);
        }

        return result;
    }
}