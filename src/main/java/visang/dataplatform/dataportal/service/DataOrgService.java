package visang.dataplatform.dataportal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import visang.dataplatform.dataportal.dto.response.dataorg.QueryResponseAllOrgData;
import visang.dataplatform.dataportal.dto.response.dataorg.QueryResponseSystemInfo;
import visang.dataplatform.dataportal.mapper.DataOrgMapper;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataOrgService {

    private final DataOrgMapper dataOrgMapper;

    public List<QueryResponseAllOrgData> getAllOrgInfo() {
        return dataOrgMapper.getAllOrgInfo();
    }

    public List<QueryResponseSystemInfo> getSystemInfo(String name) {
        return dataOrgMapper.getSystemInfo(name);
    }
}
