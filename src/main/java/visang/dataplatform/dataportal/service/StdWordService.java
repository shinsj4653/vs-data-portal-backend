package visang.dataplatform.dataportal.service;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import visang.dataplatform.dataportal.mapper.StdWordMapper;
import visang.dataplatform.dataportal.model.dto.stdcheck.request.CreateStdWordDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.request.UpdateStdWordDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.SimpleStdWordDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.StdWordDetailDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.StdWordSearchDto;
import visang.dataplatform.dataportal.utils.ElasticUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StdWordService {

    private final StdWordMapper stdWordMapper;

    @Transactional(readOnly = true)
    public List<SimpleStdWordDto> getStdWords(int pageNum) {
        return stdWordMapper.getStdWords(pageNum);
    }

    @Transactional(readOnly = true)
    public StdWordDetailDto getStdWord(int word_idx) {
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
    public void deleteStdWord(int word_idx) {
        stdWordMapper.deleteStdWord(word_idx);
    }

    public List<StdWordSearchDto> searchStdWord(int pageNum, String keyword) {

        ElasticUtil client = ElasticUtil.getInstance("localhost", 9200);
        String indexName = "tb_std_word";
        List<String> fields = List.of("word_logical_nm", "word_physical_nm", "synonym_list");

        // QueryDSL 검색결과 반환
        SearchHits searchHits = client.searchStdTable(indexName, keyword, fields, pageNum, 10);
        List<StdWordSearchDto> result = new ArrayList<>();

        // 검색결과를 TableSearchDto로 wrapping
        for (SearchHit hit : searchHits) {

            Map<String, Object> sourceMap = hit.getSourceAsMap();
            Integer word_idx = (Integer) sourceMap.get("word_idx");
            String word_logical_nm = (String) sourceMap.get("word_logical_nm");
            String word_logical_desc = (String) sourceMap.get("word_logical_desc");
            String word_physical_nm = (String) sourceMap.get("word_physical_nm");
            String word_physical_desc = (String) sourceMap.get("word_physical_desc");
            String synonym_list = (String) sourceMap.get("synonym_list");

            StdWordSearchDto docData = new StdWordSearchDto(word_idx, word_logical_nm, word_logical_desc,
                    word_physical_nm, word_physical_desc, synonym_list);

            result.add(docData);
        }

        return result;
    }
}