package visang.dataplatform.dataportal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import visang.dataplatform.dataportal.mapper.StdWordMapper;
import visang.dataplatform.dataportal.model.dto.stdcheck.request.CreateStdWordDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.request.UpdateStdWordDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.SimpleStdWordDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.StdWordDetailDto;

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

}