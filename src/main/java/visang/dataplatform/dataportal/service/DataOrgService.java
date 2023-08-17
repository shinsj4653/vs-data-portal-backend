package visang.dataplatform.dataportal.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import visang.dataplatform.dataportal.model.dto.dataorg.DataOrgDto;
import visang.dataplatform.dataportal.model.dto.dataorg.ServiceManagerDto;
import visang.dataplatform.dataportal.model.dto.dataorg.ServiceSystemInfoDto;
import visang.dataplatform.dataportal.model.query.dataorg.QueryResponseAllOrgData;
import visang.dataplatform.dataportal.model.query.dataorg.QueryResponseSystemInfo;
import visang.dataplatform.dataportal.mapper.DataOrgMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static visang.dataplatform.dataportal.service.DataMapService.convertMapToJson;
import static visang.dataplatform.dataportal.service.DataMapService.mapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataOrgService {

    private final DataOrgMapper dataOrgMapper;

    public Map<String, String> getAllOrgInfo() throws JsonProcessingException {
        List<QueryResponseAllOrgData> queryResponse = dataOrgMapper.getAllOrgInfo();
        return refactorOrgData(queryResponse);
    }

    public ServiceSystemInfoDto getSystemInfo(String name) {

        List<QueryResponseSystemInfo> queryResponse = dataOrgMapper.getSystemInfo(name);
        if (queryResponse.size() == 0){
            return null;
        } else {
            List<ServiceManagerDto> managerList = makeManagerList(queryResponse);
            ServiceSystemInfoDto result = new ServiceSystemInfoDto(
                    queryResponse.get(0).getCompany_name(),
                    queryResponse.get(0).getService_name(),
                    queryResponse.get(0).getService_web_url(),
                    queryResponse.get(0).getService_os(),
                    queryResponse.get(0).getService_was(),
                    queryResponse.get(0).getService_db(),
                    managerList
            );
            return result;
        }

    }

    public List<String> getSystemByTarget(String targetName) {
        return dataOrgMapper.getSystemByTarget(targetName);
    }

    // 리스트 형태의 데이터를 트리 구조로 변환해주는 함수
    private Map<String, String> refactorOrgData(List<QueryResponseAllOrgData> list) throws JsonProcessingException {

        int id = 0;

        DataOrgDto rootNode = new DataOrgDto("비상교육", "#00b2e2", "node-" + (id++));

        for (QueryResponseAllOrgData data : list) {
            String companyName = data.getCompany_name();
            String companyColor = data.getCompany_color();
            String companyId = "node-" + (id++);

            String serviceName = data.getService_name();
            String serviceColor = data.getService_color();
            String serviceId = "node-" + (id++);

            DataOrgDto companyNode = rootNode.findOrCreateChild(companyName, companyColor, companyId);
            companyNode.findOrCreateChild(serviceName, serviceColor, serviceId);
        }

        // Map 형태 데이터를 String으로 변환해서 파라미터로 넘겨주기
        return convertMapToJson(mapper.writeValueAsString(rootNode));
    }

    // 트리 구조로 변환해줄 때, 서비스 매니저는 여러 명 이므로 하나의 리스트 안에 매니저 정보가 담겨지도록 하는 함수
    private List<ServiceManagerDto> makeManagerList(List<QueryResponseSystemInfo> queryResponse) {
        List<ServiceManagerDto> managerList = new ArrayList<>();
        for (QueryResponseSystemInfo r : queryResponse) {
            managerList.add(new ServiceManagerDto(r.getService_mngr_tkcg(), r.getService_mngr_dept(), r.getService_mngr_name()));
        }
        return managerList;
    }
}
