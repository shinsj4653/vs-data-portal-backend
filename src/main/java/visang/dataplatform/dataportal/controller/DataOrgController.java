package visang.dataplatform.dataportal.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import visang.dataplatform.dataportal.dto.response.common.ResponseDto;
import visang.dataplatform.dataportal.dto.response.common.ResponseStatus;
import visang.dataplatform.dataportal.dto.response.common.ResponseUtil;
import visang.dataplatform.dataportal.dto.response.dataorg.DataOrgSystemInfoDto;
import visang.dataplatform.dataportal.service.DataOrgService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("dataorg")
@Api(tags = { "DataOrganization API" }, description = "데이터 기반 조직도 API")
public class DataOrgController {

    private final DataOrgService dataOrgService;
    @Operation(description = "데이터 조직도 - 원하는 서비스의 시스템 정보 보여주기")
    @GetMapping("service/systeminfo")
    public ResponseDto getSystemInfo(@RequestParam String name) {
        List<DataOrgSystemInfoDto> result = dataOrgService.getSystemInfo(name);
        if (result.size() == 0){
            return ResponseUtil.FAILURE("비상교육 내에 존재하는 서비스 명을 입력해주세요", null);

        } else {
            return ResponseUtil.SUCCESS("데이터 조직도 원하는 서비스의 시스템 정보 조회에 성공하였습니다.", result);
        }
    }

}
