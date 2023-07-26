package visang.dataplatform.dataportal.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import visang.dataplatform.dataportal.dto.response.common.ResponseDto;
import visang.dataplatform.dataportal.dto.response.common.ResponseUtil;
import visang.dataplatform.dataportal.dto.response.dataorg.QueryResponseDataOrg;
import visang.dataplatform.dataportal.dto.response.dataorg.ServiceManagerDto;
import visang.dataplatform.dataportal.dto.response.dataorg.ServiceSystemInfoDto;
import visang.dataplatform.dataportal.service.DataOrgService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("dataorg")
@Api(tags = { "DataOrganization API" }, description = "데이터 기반 조직도 API")
public class DataOrgController {

    private final DataOrgService dataOrgService;
    @Operation(description = "데이터 조직도 - 원하는 서비스의 시스템 정보 보여주기")
    @GetMapping("service/systeminfo")
    public ResponseDto<ServiceSystemInfoDto> getSystemInfo(@RequestParam String name) {
        List<QueryResponseDataOrg> queryResponse = dataOrgService.getSystemInfo(name);
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

    private List<ServiceManagerDto> makeManagerList(List<QueryResponseDataOrg> queryResponse) {
        List<ServiceManagerDto> managerList = new ArrayList<>();
        for (QueryResponseDataOrg r : queryResponse) {
            managerList.add(new ServiceManagerDto(r.getService_mngr_tkcg(), r.getService_mngr_dept(), r.getService_mngr_name()));
        }
        return managerList;
    }
}
