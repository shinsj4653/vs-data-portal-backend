package visang.dataplatform.dataportal.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import visang.dataplatform.dataportal.request.dataorg.ServiceTargetRequest;
import visang.dataplatform.dataportal.response.common.ResponseDto;
import visang.dataplatform.dataportal.response.common.ResponseUtil;
import visang.dataplatform.dataportal.model.dto.dataorg.DataOrgDto;
import visang.dataplatform.dataportal.model.dto.dataorg.ServiceManagerDto;
import visang.dataplatform.dataportal.model.dto.dataorg.ServiceSystemInfoDto;
import visang.dataplatform.dataportal.model.entity.dataorg.QueryResponseAllOrgData;
import visang.dataplatform.dataportal.model.entity.dataorg.QueryResponseSystemInfo;
import visang.dataplatform.dataportal.service.DataOrgService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static visang.dataplatform.dataportal.service.DataMapService.convertMapToJson;
import static visang.dataplatform.dataportal.service.DataMapService.mapper;


@RestController
@RequiredArgsConstructor
@RequestMapping("dataorg")
@Api(tags = { "DataOrganization API" }, description = "데이터 기반 조직도 API")
public class DataOrgController {

    private final DataOrgService dataOrgService;

    @Operation(summary = "데이터 조직도 전체 정보 조회 API", description = "데이터 기반 조직도에 포함된 컴퍼니명, 서비스명을 모두 반환해주는 API")
    @GetMapping("allorginfo")
    public ResponseDto<Map<String, String>> getAllOrgInfo() throws JsonProcessingException {
        Map<String, String> result = dataOrgService.getAllOrgInfo();
        return ResponseUtil.SUCCESS("데이터 조직도 전체 정보 조회에 성공하였습니다.", result);
    }

    @Operation(summary = "데이터 조직도 서비스 시스템 정보 조회 API", description = "데이터 기반 조직도에서 서비스 클릭 시 해당 서비스의 시스템 정보를 반환해주는 API")
    @GetMapping("service/systeminfo")
    public ResponseDto<ServiceSystemInfoDto> getSystemInfo(@RequestParam String name) {
        ServiceSystemInfoDto result = dataOrgService.getSystemInfo(name);
        if (result == null){
            return ResponseUtil.FAILURE("비상교육 내에 존재하는 서비스 명을 입력하거나, 입력 값을 다시 한 번 확인해주시길 바랍니다.", null);
        } else {
            return ResponseUtil.SUCCESS("데이터 조직도 원하는 서비스의 시스템 정보 조회에 성공하였습니다.", result);
        }

    }

    @Operation(summary = "데이터 조직도 타켓 기준 서비스 명 조회 API", description = "데이터 기반 조직도에서 서비스 대상을 눌렀을 시, 해당 대상과 관련된 서비스 명 반환해주는 API")
    @PostMapping("service/target")
    public ResponseDto<List<String>> getSystemByTarget(@RequestBody ServiceTargetRequest req){
        List<String> result = dataOrgService.getSystemByTarget(req.getTarget_name());
        return ResponseUtil.SUCCESS("해당 타켓에 해당하는 서비스 명 정보 조회에 성공하였습니다.", result);
    }

}
