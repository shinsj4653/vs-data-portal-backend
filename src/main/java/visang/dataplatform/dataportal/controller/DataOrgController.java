package visang.dataplatform.dataportal.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import visang.dataplatform.dataportal.dto.response.common.ResponseDto;
import visang.dataplatform.dataportal.dto.response.common.ResponseUtil;
import visang.dataplatform.dataportal.dto.response.datamap.DataMapDto;
import visang.dataplatform.dataportal.dto.response.dataorg.*;
import visang.dataplatform.dataportal.service.DataOrgService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static visang.dataplatform.dataportal.controller.DataMapController.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("dataorg")
@Api(tags = { "DataOrganization API" }, description = "데이터 기반 조직도 API")
public class DataOrgController {

    private final DataOrgService dataOrgService;

    @Operation(summary = "데이터 조직도 전체 정보 조회 API", description = "데이터 기반 조직도 메뉴를 클릭 시 나오는 조직도에 포함된 컴퍼니명, 서비스명을 반환해주는 API")
    @GetMapping("allorginfo")
    public ResponseDto<Map<String, String>> getAllOrgInfo() throws JsonProcessingException {
        List<QueryResponseAllOrgData> queryResponse = dataOrgService.getAllOrgInfo();
        return ResponseUtil.SUCCESS("데이터 조직도 전체 정보 조회에 성공하였습니다.", refactorOrgData(queryResponse));
    }

    @Operation(summary = "데이터 조직도 서비스 시스템 정보 조회 API", description = "데이터 기반 조직도 메뉴를 클릭 시 나오는 조직도 화면 상에서, 원하는 서비스 클릭 시 해당 서비스의 시스템 정보를 반환해주는 API")
    @GetMapping("service/systeminfo")
    public ResponseDto<ServiceSystemInfoDto> getSystemInfo(@RequestParam String name) {
        List<QueryResponseSystemInfo> queryResponse = dataOrgService.getSystemInfo(name);
        if (queryResponse.size() == 0){
            return ResponseUtil.FAILURE("비상교육 내에 존재하는 서비스 명을 입력하거나, 입력 값을 다시 한 번 확인해주시길 바랍니다.", null);
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
            return ResponseUtil.SUCCESS("데이터 조직도 원하는 서비스의 시스템 정보 조회에 성공하였습니다.", result);
        }
    }

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

    private List<ServiceManagerDto> makeManagerList(List<QueryResponseSystemInfo> queryResponse) {
        List<ServiceManagerDto> managerList = new ArrayList<>();
        for (QueryResponseSystemInfo r : queryResponse) {
            managerList.add(new ServiceManagerDto(r.getService_mngr_tkcg(), r.getService_mngr_dept(), r.getService_mngr_name()));
        }
        return managerList;
    }
}
