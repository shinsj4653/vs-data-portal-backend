package visang.dataplatform.dataportal.service;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import visang.dataplatform.dataportal.mapper.StdWordMapper;
import visang.dataplatform.dataportal.model.dto.stdcheck.request.CreateStdWordDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.request.UpdateStdWordDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.SimpleStdWordDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.StdWordDetailDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.StdWordSearchDto;
import visang.dataplatform.dataportal.utils.ElasticUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StdWordService {

    private final StdWordMapper stdWordMapper;

    @Transactional(readOnly = true)
    public List<SimpleStdWordDto> getStdWords(int pageNum) {
        return stdWordMapper.getStdWords(pageNum);
    }

    @Transactional(readOnly = true)
    public StdWordDetailDto getStdWord(Long word_idx) {
        return stdWordMapper.getStdWord(word_idx);
    }

    @Transactional
    public void createStdWord(CreateStdWordDto createStdWordDto) {
        stdWordMapper.createStdWord(createStdWordDto);
    }

    @Transactional
    public void updateStdWord(UpdateStdWordDto updateStdWordDto) {
        stdWordMapper.updateStdWord(updateStdWordDto);
    }

    @Transactional
    public void deleteStdWord(Long word_idx) {
        stdWordMapper.deleteStdWord(word_idx);
    }

    public List<StdWordSearchDto> searchStdWord(int pageNum, String keyword) throws IOException {

        ElasticUtil client = ElasticUtil.getInstance("localhost", 9200);
        String indexName = "tb_std_word";
        List<String> fields = List.of("word_logical_nm", "word_physical_nm", "synonym_list");

        // QueryDSL 검색결과 반환
        SearchResponse<StdWordSearchDto> searchHits = client.getTotalTableSearch(indexName, keyword, fields, pageNum, 10, StdWordSearchDto.class);
        List<StdWordSearchDto> result = new ArrayList<>();

        // 검색결과를 TableSearchDto로 wrapping
        for (Hit<StdWordSearchDto> hit : searchHits.hits().hits()) {

            Long word_idx = hit.source().getWord_idx();
            String word_logical_nm = hit.source().getWord_logical_nm();
            String word_logical_desc = hit.source().getWord_logical_desc();
            String word_physical_nm = hit.source().getWord_physical_nm();
            String word_physical_desc = hit.source().getWord_physical_desc();
            String synonym_list = hit.source().getSynonym_list();

            StdWordSearchDto docData = new StdWordSearchDto(word_idx, word_logical_nm, word_logical_desc,
                    word_physical_nm, word_physical_desc, synonym_list);

            result.add(docData);
        }

        return result;
    }
}