package visang.dataplatform.dataportal.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import visang.dataplatform.dataportal.dto.response.DataByCategoryDto;
import visang.dataplatform.dataportal.dto.response.DataOrgSystemInfoDto;
import visang.dataplatform.dataportal.service.DataOrgService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("dataorg")
@Api(tags = { "DataOrganization API" }, description = "데이터 기반 조직도 API")
public class DataOrgController {

    private final DataOrgService dataOrgService;
    @Operation(description = "데이터 조직도 - 원하는 서비스의 시스템 정보 보여주기")
    @GetMapping("service/systeminfo")
    public List<DataOrgSystemInfoDto> getSystemInfo(@RequestParam String name) {
        List<DataOrgSystemInfoDto> list = dataOrgService.getSystemInfo(name);
        return list;
    }

}
