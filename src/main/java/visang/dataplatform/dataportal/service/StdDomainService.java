package visang.dataplatform.dataportal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import visang.dataplatform.dataportal.mapper.StdDomainMapper;
import visang.dataplatform.dataportal.model.dto.stdcheck.request.CreateStdDomainDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.request.UpdateStdDomainDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.SimpleStdDomainDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.StdDomainDetailDto;

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

}