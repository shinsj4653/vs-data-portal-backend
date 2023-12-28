package visang.dataplatform.dataportal.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import visang.dataplatform.dataportal.model.dto.stdcheck.request.CreateStdDomainDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.request.UpdateStdDomainDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.SimpleStdDomainDto;
import visang.dataplatform.dataportal.model.dto.stdcheck.response.StdDomainDetailDto;

import java.util.List;

@Mapper
public interface StdDomainMapper {

    List<SimpleStdDomainDto> getStdDomains(@Param("page_num") int pageNum);

    StdDomainDetailDto getStdDomain(@Param("domain_idx") int id);

    void createStdDomain(CreateStdDomainDto createStdDomainDto);

    void updateStdDomain(UpdateStdDomainDto updateStdDomainDto);

    void deleteStdDomain(@Param("domain_idx") int id);

}