package visang.dataplatform.dataportal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import visang.dataplatform.dataportal.dto.response.datamap.QueryResponseDataMap;
import visang.dataplatform.dataportal.mapper.DataMapMapper;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataMapService {

    private final DataMapMapper dataMapMapper;

    public List<QueryResponseDataMap> getMapMainData() {
        return dataMapMapper.getMapMainData();
    }
    public List<QueryResponseDataMap> getMapSubData() {
        return dataMapMapper.getMapSubData();
    }
    public List<String> getPrimaryDataset() {
        return dataMapMapper.getPrimaryDataset();
    }

}
