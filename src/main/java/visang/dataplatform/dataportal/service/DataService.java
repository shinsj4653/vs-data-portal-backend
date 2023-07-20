package visang.dataplatform.dataportal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import visang.dataplatform.dataportal.dto.response.DataByCategoryDto;
import visang.dataplatform.dataportal.mapper.DataMapper;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataService {

    private final DataMapper dataMapper;

    public List<DataByCategoryDto> getChartData() {
        return dataMapper.getChartData();
    }

}
