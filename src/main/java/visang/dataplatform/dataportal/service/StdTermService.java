package visang.dataplatform.dataportal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import visang.dataplatform.dataportal.mapper.StdTermMapper;
import visang.dataplatform.dataportal.model.dto.stdcheck.request.CreateStdTermDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.request.UpdateStdTermDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.SimpleStdTermDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.StdTermDetailDto;

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

}