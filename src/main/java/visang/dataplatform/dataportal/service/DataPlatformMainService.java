package visang.dataplatform.dataportal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import visang.dataplatform.dataportal.mapper.DataPlatformMainMapper;
import visang.dataplatform.dataportal.model.dto.dpmain.DatasetSearchDto;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataPlatformMainService {

    private final DataPlatformMainMapper dataPlatformMapper;

    public List<DatasetSearchDto> getServiceList(String keyword) {
        return dataPlatformMapper.getServiceList(keyword);
    }
}
