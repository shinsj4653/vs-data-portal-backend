package visang.dataplatform.dataportal.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import visang.dataplatform.dataportal.dto.response.common.ResponseDto;
import visang.dataplatform.dataportal.dto.response.common.ResponseUtil;
import visang.dataplatform.dataportal.dto.response.dataorg.QueryResponseAllOrgData;
import visang.dataplatform.dataportal.dto.response.dataorg.QueryResponseSystemInfo;
import visang.dataplatform.dataportal.dto.response.dataorg.ServiceManagerDto;
import visang.dataplatform.dataportal.dto.response.dataorg.ServiceSystemInfoDto;
import visang.dataplatform.dataportal.service.DataOrgService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("dataorg")
@Api(tags = { "DataOrganization API" }, description = "데이터 기반 조직도 API")
public class DataOrgController {

    private final DataOrgService dataOrgService;

    @Operation(summary = "데이터 조직도 전체 정보 조회 API", description = "데이터 기반 조직도 메뉴를 클릭 시 나오는 조직도에 포함된 컴퍼니명, 서비스명을 반환해주는 API")
    @GetMapping("allorginfo")
    public ResponseDto<Map<String, List<String>>> getAllOrgInfo() {
        List<QueryResponseAllOrgData> queryResponse = dataOrgService.getAllOrgInfo();

        return ResponseUtil.SUCCESS("데이터 조직도 전체 정보 조회에 성공하였습니다.", refactorOrgData(queryResponse));

    }

    @Operation(summary = "데이터 조직도 서비스 시스템 정보 조회 API", description = "데이터 기반 조직도 메뉴를 클릭 시 나오는 조직도 화면 상에서, 원하는 서비스 클릭 시 해당 서비스의 시스템 정보를 반환해주는 API")
    @GetMapping("service/systeminfo")
    public ResponseDto<ServiceSystemInfoDto> getSystemInfo(@RequestParam String name) {
        List<QueryResponseSystemInfo> queryResponse = dataOrgService.getSystemInfo(name);
        if (queryResponse.size() == 0){
            return ResponseUtil.FAILURE("비상교육 내에 존재하는 서비스 명을 입력해주세요", null);
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

    private Map<String, List<String>> refactorOrgData(List<QueryResponseAllOrgData> queryResponse) {

        Map<String, List<String>> orgDataMap = new HashMap<>();

        for (QueryResponseAllOrgData q : queryResponse) {
            if (orgDataMap.containsKey(q.getCompany_name())) {
                List<String> serviceNames = orgDataMap.get(q.getCompany_name());
                serviceNames.add(q.getService_name());
            } else {
                List<String> serviceNames = new ArrayList<>();
                serviceNames.add(q.getService_name());
                orgDataMap.put(q.getCompany_name(), serviceNames);
            }
        }
        return orgDataMap;
    }

    private List<ServiceManagerDto> makeManagerList(List<QueryResponseSystemInfo> queryResponse) {
        List<ServiceManagerDto> managerList = new ArrayList<>();
        for (QueryResponseSystemInfo r : queryResponse) {
            managerList.add(new ServiceManagerDto(r.getService_mngr_tkcg(), r.getService_mngr_dept(), r.getService_mngr_name()));
        }
        return managerList;
    }
}
