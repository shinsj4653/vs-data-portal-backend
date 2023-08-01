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

import static visang.dataplatform.dataportal.controller.DataMapController.convertMapToJson;
import static visang.dataplatform.dataportal.controller.DataMapController.removeTrailingCommas;

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
            DataOrgDto serviceNode = companyNode.findOrCreateChild(serviceName, serviceColor, serviceId);
        }

        return convertMapToJson(rootNode);
    }

    public static Map<String, String> convertMapToJson(DataOrgDto rootNode) throws JsonProcessingException {
        // Map 형태 데이터를 String으로 변환
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(rootNode);

        // "loc": null와 "children" : null 인 부분을 String 상에서 제거
        String locRemoved = json.replaceAll("\"loc\"\\s*:\\s*null(,)?", "");
        String childrenRemoved = locRemoved.replaceAll("\"children\"\\s*:\\s*\\[\\]\\s*(,)?", "");

        // String을 JsonObject로 파싱할 때, 끝 부분에 따라오는 콤마들을 제거해줘야 에러가 안남
        String cleanedJsonString = removeTrailingCommas(childrenRemoved);
        Map<String, String> map = mapper.readValue(cleanedJsonString, Map.class);

        return map;

    }

    private List<ServiceManagerDto> makeManagerList(List<QueryResponseSystemInfo> queryResponse) {
        List<ServiceManagerDto> managerList = new ArrayList<>();
        for (QueryResponseSystemInfo r : queryResponse) {
            managerList.add(new ServiceManagerDto(r.getService_mngr_tkcg(), r.getService_mngr_dept(), r.getService_mngr_name()));
        }
        return managerList;
    }
}
